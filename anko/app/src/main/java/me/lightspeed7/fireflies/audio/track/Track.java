package me.lightspeed7.fireflies.audio.track;

public interface Track {

    void setVolume(int volume);

    int getVolume();

    void play(int sampleRate, byte[] soundData) throws Exception;

    void stop();
}
