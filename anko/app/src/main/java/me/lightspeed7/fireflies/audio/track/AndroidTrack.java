package me.lightspeed7.fireflies.audio.track;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class AndroidTrack implements Track {

    private AudioTrack audioTrack = null;
    private int volume;
    private int audTrackBufferSize = 0;

    public AndroidTrack(int volume) {
        this.volume = volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }

    public void stop() {
        if ( audioTrack != null) {
            audioTrack.pause();
            audioTrack.flush();
            audioTrack.release();
            audioTrack = null;
        }
    }

    public void play(int sampleRate, byte[] soundData) {
        int bufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

        //if buffer-size changing or no previous obj then allocate:
        if (bufferSize != audTrackBufferSize || audioTrack == null) {
            if (audioTrack != null) {
                //release previous object
                audioTrack.pause();
                audioTrack.flush();
                audioTrack.release();
            }

            //allocate new object:
            //noinspection deprecation
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, bufferSize,
                    AudioTrack.MODE_STREAM);
            audTrackBufferSize = bufferSize;
        }

        float gain = (float) (volume / 100.0);
        //noinspection deprecation
        audioTrack.setStereoVolume(gain, gain);

        audioTrack.play();
        audioTrack.write(soundData, 0, soundData.length);
    }
}
