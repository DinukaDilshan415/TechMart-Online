package me.dinuka.tmo.util.monitor;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.util.concurrent.atomic.AtomicLong;

@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@PerformanceMonitored
public class PerformanceInterceptor {

    private static final AtomicLong totalTime  = new AtomicLong(0);
    private static final AtomicLong callCount  = new AtomicLong(0);
    private static final AtomicLong peakTime   = new AtomicLong(0);
    private static final AtomicLong minTime    = new AtomicLong(Long.MAX_VALUE); // Added for Min tracking
    private static final AtomicLong lastCallTime = new AtomicLong(0);

    @AroundInvoke
    public Object measureTime(InvocationContext ctx) throws Exception {
        long start = System.currentTimeMillis();
        try {
            return ctx.proceed();
        } finally {
            long elapsed = System.currentTimeMillis() - start;

            totalTime.addAndGet(elapsed);
            callCount.incrementAndGet();
            lastCallTime.set(elapsed);

            // Track peak (max) response time
            long currentPeak = peakTime.get();
            if (elapsed > currentPeak) {
                peakTime.compareAndSet(currentPeak, elapsed);
            }

            // Track lowest (min) response time
            long currentMin = minTime.get();
            while (elapsed < currentMin) {
                if (minTime.compareAndSet(currentMin, elapsed)) break;
                currentMin = minTime.get();
            }
        }
    }

    public static double getAvgResponseTime() {
        long count = callCount.get();
        return count == 0 ? 0.0 : (double) totalTime.get() / count;
    }

    public static long getPeakResponseTime() { return peakTime.get(); }

    public static long getMinResponseTime() {
        long currentMin = minTime.get();
        return currentMin == Long.MAX_VALUE ? 0L : currentMin;
    }

    public static long getLastCallTime() { return lastCallTime.get(); }
    public static long getCallCount() { return callCount.get(); }

    public static void reset() {
        totalTime.set(0);
        callCount.set(0);
        peakTime.set(0);
        minTime.set(Long.MAX_VALUE);
        lastCallTime.set(0);
    }
}
