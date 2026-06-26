package me.dinuka.tmo.util.monitor;

import java.util.concurrent.atomic.AtomicInteger;

public class OrderTracker {

    private static final AtomicInteger activeCount = new AtomicInteger(0);
    private static final AtomicInteger totalProcessed = new AtomicInteger(0);

    public static void orderStarted() {
        activeCount.incrementAndGet();
        totalProcessed.incrementAndGet();
    }

    public static void orderCompleted() {
        activeCount.decrementAndGet();
    }

    public static int getActiveCount() {
        return activeCount.get();
    }

    public static int getTotalProcessed() {
        return totalProcessed.get();
    }

    public static void reset() {
        activeCount.set(0);
        totalProcessed.set(0);
    }
}