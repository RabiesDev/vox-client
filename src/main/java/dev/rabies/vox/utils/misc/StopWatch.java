package dev.rabies.vox.utils.misc;

public class StopWatch {
    private long lastTime = 0L;

    public void reset() {
        lastTime = System.currentTimeMillis();
    }

    public long elapsed() {
        return System.currentTimeMillis() - lastTime;
    }

    public boolean finished(float ms, boolean reset) {
        boolean elapsed = elapsed() >= ms;
        if (elapsed && reset) reset();
        return elapsed;
    }
}
