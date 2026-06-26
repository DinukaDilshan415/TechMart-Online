package me.dinuka.tmo.util.monitor;

import java.util.concurrent.atomic.AtomicInteger;

public class DbConnectionTracker {

    public static final int POOL_MIN  = 50;
    public static final int POOL_MAX  = 200;
    public static final int POOL_STEP = 2;

    private static final AtomicInteger inUse = new AtomicInteger(0);

    public static void connectionAcquired() {
        inUse.incrementAndGet();
    }

    public static void connectionReleased() {
        if (inUse.get() > 0) inUse.decrementAndGet();
    }

    public static int getInUse()   { return inUse.get(); }
    public static int getPoolMin() { return POOL_MIN; }
    public static int getPoolMax() { return POOL_MAX; }

    public static double getUsagePercent() {
        return Math.round((inUse.get() / (double) POOL_MAX) * 100.0);
    }
}