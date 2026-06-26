package me.dinuka.tmo.util.monitor;

import java.util.concurrent.atomic.AtomicInteger;

public class SessionTracker {

    private static final AtomicInteger activeCount = new AtomicInteger(0);
    private static final AtomicInteger totalCreated = new AtomicInteger(0);

    // Call this when a new user session is created
    public static void sessionCreated() {
        activeCount.incrementAndGet();
        totalCreated.incrementAndGet();
    }

    // Call this when a user session ends
    public static void sessionDestroyed() {
        if (activeCount.get() > 0) {
            activeCount.decrementAndGet();
        }
    }

    public static int getActiveCount() {
        return activeCount.get();
    }

    public static int getTotalCreated() {
        return totalCreated.get();
    }

    public static void reset() {
        activeCount.set(0);
        totalCreated.set(0);
    }
}