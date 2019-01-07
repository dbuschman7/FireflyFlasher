package me.lightspeed7.fireflies.audio;

import me.lightspeed7.fireflies.audio.track.AndroidTrack;
import me.lightspeed7.fireflies.audio.track.Track;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class TonePlayer {
    private double toneFreqInHz = 440;
    private final Object toneFreqInHzSyncObj = new Object();

    private Track track = new AndroidTrack(100);

    boolean isPlaying = false;
    Thread playerWorker;

    private double lastToneFreqInHz = 0.0;
    private int lastNumSamplesCount = 0;
    private double[] lastDoubleSamples = null;
    private byte[] lastSoundBytes = null;


    public TonePlayer() {
    }

    public TonePlayer(double toneFreqInHz) {
        this.toneFreqInHz = toneFreqInHz;
    }

    public void play() {
        if (isPlaying)
            return;

        stop();

        isPlaying = true;
        asyncPlayTrack();
    }

    public void stop() {
        isPlaying = false;
        if (track == null)
            return;

        tryStopPlayer();
    }

    public void setVolume(int volume) {
        this.track.setVolume(volume);
    }

    public int getVolume() {
        return track.getVolume();
    }

    public double getToneFreqInHz() {
        synchronized (toneFreqInHzSyncObj) {
            return toneFreqInHz;
        }
    }

    public void setToneFreqInHz(double toneFreqInHz) {
        synchronized (toneFreqInHzSyncObj) {
            this.toneFreqInHz = toneFreqInHz;
        }
    }

    protected abstract void asyncPlayTrack();

    protected void tryStopPlayer() {
        isPlaying = false;
        try {
            if (playerWorker != null)
                playerWorker.interrupt();

            // pause() appears to be more snappy in audio cutoff than stop()
            track.stop();

        } catch (IllegalStateException e) {
            // Likely issue with concurrency, doesn't matter, since means it's stopped anyway
            // so we just eat the exception
        }
    }

    /**
     * below from http://stackoverflow.com/questions/2413426/playing-an-arbitrary-tone-with-android
     */
    protected void playTone(double seconds, boolean continuousFlag) {
        int sampleRate = 8000;

        int numSamples = (int) Math.ceil(seconds * sampleRate);
        final double freqInHz = getToneFreqInHz();

        double[] sample;
        byte[] soundData;
        if (numSamples == lastNumSamplesCount) {
            // if same tone then
            if (freqInHz == lastToneFreqInHz) {
                // repeat with same data
                playSound(sampleRate, lastSoundBytes);
                return;
            }

            // different tone freq but same duration
            // reuse arrays
            sample = lastDoubleSamples;
            soundData = lastSoundBytes;
        } else {  //different duration; create (and save) new arrays
            sample = lastDoubleSamples = new double[numSamples];
            soundData = lastSoundBytes = new byte[2 * numSamples];
            lastNumSamplesCount = numSamples;
        }
        lastToneFreqInHz = freqInHz;

        // Fill the sample array
        for (int i = 0; i < numSamples; ++i)
            sample[i] = Math.sin(freqInHz * 2 * Math.PI * i / (sampleRate));

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalized.
        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        int i = 0;

        // Amplitude ramp as a percent of sample count
        //  (smaller ramp for continuous; trying for cleaner sound)
        int ramp = numSamples / (continuousFlag ? 200 : 20);

        // Ramp amplitude up (to avoid clicks)
        for (i = 0; i < ramp; ++i) {
            double dVal = sample[i];
            // Ramp up to maximum
            final short val = (short) ((dVal * 32767 * i / ramp));
            // in 16 bit wav PCM, first byte is the low order byte
            soundData[idx++] = (byte) (val & 0x00ff);
            soundData[idx++] = (byte) ((val & 0xff00) >>> 8);
        }

        // Max amplitude for most of the samples
        for (i = i; i < numSamples - ramp; ++i) {
            double dVal = sample[i];
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            soundData[idx++] = (byte) (val & 0x00ff);
            soundData[idx++] = (byte) ((val & 0xff00) >>> 8);
        }

        // Ramp amplitude down
        for (i = i; i < numSamples; ++i) {
            double dVal = sample[i];
            // Ramp down to zero
            final short val = (short) ((dVal * 32767 * (numSamples - i) / ramp));
            // in 16 bit wav PCM, first byte is the low order byte
            soundData[idx++] = (byte) (val & 0x00ff);
            soundData[idx++] = (byte) ((val & 0xff00) >>> 8);
        }

        playSound(sampleRate, soundData);
    }

    protected void playTone(double seconds) {
        playTone(seconds, false);
    }

    protected void playSound(int sampleRate, byte[] soundData) {
        try {
            track.play(sampleRate, soundData);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "tone player", e);
        }
    }
}
