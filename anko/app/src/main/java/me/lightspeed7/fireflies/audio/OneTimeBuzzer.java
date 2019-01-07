package me.lightspeed7.fireflies.audio;

/**
 * A buzzer that will end after the set duration has ended or stop() is called.
 * Default duration is 5 seconds.
 */
public class OneTimeBuzzer extends TonePlayer {
    private double duration = 5;

    public OneTimeBuzzer(double duration) {
        this.duration = duration;
    }

    public OneTimeBuzzer() {
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    protected void asyncPlayTrack() {
        playerWorker = new Thread(new Runnable() {
            public void run() {
                playTone(duration);
                stop();
            }
        });

        playerWorker.start();
    }
}