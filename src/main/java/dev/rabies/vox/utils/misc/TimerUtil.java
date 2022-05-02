package dev.rabies.vox.utils.misc;

public class TimerUtil {

    private long prevMS = 0L;

    public boolean delay(float milliSec) {
        return ((float) this.getIncremental((getTime() - prevMS), 50.0D) >= milliSec);
    }

    public void reset() {
        prevMS = getTime();
    }

    public long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public long getDifference() {
        return getTime() - prevMS;
    }

    private double getIncremental(double val, double inc) {
        double one = 1.0D / inc;
        return Math.round(val * one) / one;
    }
}
