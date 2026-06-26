package me.dinuka.tmo.util.monitor;

import java.util.Map;

public class MetricsDTO {
    private double avgResponseTimeMs;
    private int activeSessionCount;
    private int activeOrders;
    private int jmsMessageCount;
    private int mdbProcessingRate;
    private int dbConnectionsInUse;
    private int dbConnectionsMax;
    private long jmsQueueDepth;
    private double heapUsedMB;
    private double heapMaxMB;
    private double uptimePercent;
    private int    dbPoolMin;
    private double dbUsagePercent;
    private Map<String, Map<String, Object>> operationStats;
    private long minResponseTimeMs;
    private long maxResponseTimeMs;
    private int asyncTimeoutCount;
    private long asyncExecutionDurationMs;
    private double dbConnectionAcquisitionWaitMs;
    private int activeMdbPoolCount;
    private int maxMdbPoolSize;

    // Getters and Setters
    public double getAvgResponseTimeMs() { return avgResponseTimeMs; }
    public void setAvgResponseTimeMs(double v) { this.avgResponseTimeMs = v; }

    public int getActiveSessionCount() { return activeSessionCount; }
    public void setActiveSessionCount(int v) { this.activeSessionCount = v; }

    public int getActiveOrders() { return activeOrders; }
    public void setActiveOrders(int v) { this.activeOrders = v; }

    public int getJmsMessageCount() { return jmsMessageCount; }
    public void setJmsMessageCount(int v) { this.jmsMessageCount = v; }

    public int getMdbProcessingRate() { return mdbProcessingRate; }
    public void setMdbProcessingRate(int v) { this.mdbProcessingRate = v; }

    public int getDbConnectionsInUse() { return dbConnectionsInUse; }
    public void setDbConnectionsInUse(int v) { this.dbConnectionsInUse = v; }

    public int getDbConnectionsMax() { return dbConnectionsMax; }
    public void setDbConnectionsMax(int v) { this.dbConnectionsMax = v; }

    public long getJmsQueueDepth() { return jmsQueueDepth; }
    public void setJmsQueueDepth(long v) { this.jmsQueueDepth = v; }

    public double getHeapUsedMB() { return heapUsedMB; }
    public void setHeapUsedMB(double v) { this.heapUsedMB = v; }

    public double getHeapMaxMB() { return heapMaxMB; }
    public void setHeapMaxMB(double v) { this.heapMaxMB = v; }

    public double getUptimePercent() { return uptimePercent; }
    public void setUptimePercent(double v) { this.uptimePercent = v; }

    public int    getDbPoolMin()        { return dbPoolMin; }
    public void   setDbPoolMin(int v)   { this.dbPoolMin = v; }

    public double getDbUsagePercent()        { return dbUsagePercent; }
    public void   setDbUsagePercent(double v){ this.dbUsagePercent = v; }

    public Map<String, Map<String, Object>> getOperationStats() {
        return operationStats;
    }
    public void setOperationStats(Map<String, Map<String, Object>> v) {
        this.operationStats = v;
    }

    public long getMinResponseTimeMs() { return minResponseTimeMs; }
    public void setMinResponseTimeMs(long v) { this.minResponseTimeMs = v; }

    public long getMaxResponseTimeMs() { return maxResponseTimeMs; }
    public void setMaxResponseTimeMs(long v) { this.maxResponseTimeMs = v; }

    public int getAsyncTimeoutCount() { return asyncTimeoutCount; }
    public void setAsyncTimeoutCount(int v) { this.asyncTimeoutCount = v; }

    public long getAsyncExecutionDurationMs() { return asyncExecutionDurationMs; }
    public void setAsyncExecutionDurationMs(long v) { this.asyncExecutionDurationMs = v; }

    public double getDbConnectionAcquisitionWaitMs() { return dbConnectionAcquisitionWaitMs; }
    public void setDbConnectionAcquisitionWaitMs(double v) { this.dbConnectionAcquisitionWaitMs = v; }

    public int getActiveMdbPoolCount() { return activeMdbPoolCount; }
    public void setActiveMdbPoolCount(int v) { this.activeMdbPoolCount = v; }

    public int getMaxMdbPoolSize() { return maxMdbPoolSize; }
    public void setMaxMdbPoolSize(int v) { this.maxMdbPoolSize = v;}
}
