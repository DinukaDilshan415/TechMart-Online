package me.dinuka.tmo.util.monitor;


import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MdbMetrics {

    private static final AtomicInteger totalProcessed = new AtomicInteger(0);
    private static final AtomicInteger failedCount = new AtomicInteger(0);
    private static final AtomicLong lastMinuteCount = new AtomicLong(0);
    private static volatile long windowStart = System.currentTimeMillis();

    // Call this inside your MDB onMessage()
    public static void messageProcessed() {
        totalProcessed.incrementAndGet();
        trackRate();
    }

    public static void messageFailed() {
        failedCount.incrementAndGet();
    }

    // Tracks messages per minute
    private static void trackRate() {
        long now = System.currentTimeMillis();
        long elapsed = now - windowStart;

        if (elapsed >= 60_000) {
            // reset window every minute
            lastMinuteCount.set(1);
            windowStart = now;
        } else {
            lastMinuteCount.incrementAndGet();
        }
    }

    // Returns messages processed in current minute window
    public static int getRate() {
        return lastMinuteCount.intValue();
    }

    public static int getTotalProcessed() {
        return totalProcessed.get();
    }

    public static int getFailedCount() {
        return failedCount.get();
    }

    public static void reset() {
        totalProcessed.set(0);
        failedCount.set(0);
        lastMinuteCount.set(0);
        windowStart = System.currentTimeMillis();
    }
}