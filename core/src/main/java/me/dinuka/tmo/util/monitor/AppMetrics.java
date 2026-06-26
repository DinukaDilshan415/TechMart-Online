package me.dinuka.tmo.util.monitor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class AppMetrics {

    // Stores total execution time per operation
    private static final ConcurrentHashMap<String, AtomicLong> totalTime
            = new ConcurrentHashMap<>();

    // Stores how many times each operation was called
    private static final ConcurrentHashMap<String, AtomicLong> callCount
            = new ConcurrentHashMap<>();

    // Stores how many times each operation failed
    private static final ConcurrentHashMap<String, AtomicLong> failCount
            = new ConcurrentHashMap<>();

    // Stores peak (slowest) time per operation
    private static final ConcurrentHashMap<String, AtomicLong> peakTime
            = new ConcurrentHashMap<>();

    // -------------------------------------------------------
    // Call this when any operation SUCCEEDS
    // operationName = any string you choose eg "createOrder"
    // -------------------------------------------------------
    public static void recordSuccess(String operationName, long elapsedMs) {
        totalTime.computeIfAbsent(operationName, k -> new AtomicLong(0))
                .addAndGet(elapsedMs);
        callCount.computeIfAbsent(operationName, k -> new AtomicLong(0))
                .incrementAndGet();

        // Update peak if this call was slower
        peakTime.computeIfAbsent(operationName, k -> new AtomicLong(0))
                .accumulateAndGet(elapsedMs, Math::max);
    }

    // -------------------------------------------------------
    // Call this when any operation FAILS
    // -------------------------------------------------------
    public static void recordFailure(String operationName, long elapsedMs) {
        totalTime.computeIfAbsent(operationName, k -> new AtomicLong(0))
                .addAndGet(elapsedMs);
        callCount.computeIfAbsent(operationName, k -> new AtomicLong(0))
                .incrementAndGet();
        failCount.computeIfAbsent(operationName, k -> new AtomicLong(0))
                .incrementAndGet();
    }

    // -------------------------------------------------------
    // Getters
    // -------------------------------------------------------
    public static double getAvgTime(String operationName) {
        AtomicLong count = callCount.get(operationName);
        AtomicLong time  = totalTime.get(operationName);
        if (count == null || count.get() == 0) return 0.0;
        return (double) time.get() / count.get();
    }

    public static long getCallCount(String operationName) {
        AtomicLong v = callCount.get(operationName);
        return v == null ? 0 : v.get();
    }

    public static long getFailCount(String operationName) {
        AtomicLong v = failCount.get(operationName);
        return v == null ? 0 : v.get();
    }

    public static long getPeakTime(String operationName) {
        AtomicLong v = peakTime.get(operationName);
        return v == null ? 0 : v.get();
    }

    public static double getSuccessRate(String operationName) {
        long total = getCallCount(operationName);
        long fails = getFailCount(operationName);
        if (total == 0) return 100.0;
        return ((double)(total - fails) / total) * 100.0;
    }

    // Returns ALL operations as a map — for JSON in MetricsServlet
    public static Map<String, Map<String, Object>> getAllStats() {
        Map<String, Map<String, Object>> result = new HashMap<>();
        for (String key : callCount.keySet()) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("callCount",   getCallCount(key));
            stat.put("failCount",   getFailCount(key));
            stat.put("avgMs",       Math.round(getAvgTime(key)));
            stat.put("peakMs",      getPeakTime(key));
            stat.put("successRate", Math.round(getSuccessRate(key)));
            result.put(key, stat);
        }
        return result;
    }
}
