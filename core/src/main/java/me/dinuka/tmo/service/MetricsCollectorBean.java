package me.dinuka.tmo.service;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import me.dinuka.tmo.util.monitor.*;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

@Stateless
@MetricsCollectorQualifier
public class MetricsCollectorBean {

    public MetricsDTO collect() {
        MetricsDTO dto = new MetricsDTO();

        dto.setAvgResponseTimeMs(PerformanceInterceptor.getAvgResponseTime());
        dto.setActiveSessionCount(SessionTracker.getActiveCount());
        dto.setActiveOrders(OrderTracker.getActiveCount());
        dto.setJmsMessageCount(JmsMetrics.getTotalMessages());
        dto.setMdbProcessingRate(MdbMetrics.getRate());

        MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
        long usedHeap = memBean.getHeapMemoryUsage().getUsed();
        long maxHeap  = memBean.getHeapMemoryUsage().getMax();
        dto.setHeapUsedMB(Math.round(usedHeap / 1024.0 / 1024.0));
        dto.setHeapMaxMB(Math.round(maxHeap  / 1024.0 / 1024.0));

        dto.setDbConnectionsInUse(DbConnectionTracker.getInUse());
        dto.setDbConnectionsMax(DbConnectionTracker.POOL_MAX);
        dto.setDbPoolMin(DbConnectionTracker.POOL_MIN);
        dto.setDbUsagePercent(DbConnectionTracker.getUsagePercent());

        dto.setJmsQueueDepth(JmsMetrics.getTotalMessages() - JmsMetrics.getTotalMessagesReceived());
        dto.setUptimePercent(99.9);
        dto.setOperationStats(AppMetrics.getAllStats());

        dto.setMinResponseTimeMs(PerformanceInterceptor.getMinResponseTime());
        dto.setMaxResponseTimeMs(PerformanceInterceptor.getPeakResponseTime());

        dto.setAsyncTimeoutCount(MdbMetrics.getFailedCount());
        dto.setAsyncExecutionDurationMs(PerformanceInterceptor.getLastCallTime());

        double simulatedWaitMs = DbConnectionTracker.getInUse() > 150 ? 4.2 : 0.8;
        dto.setDbConnectionAcquisitionWaitMs(simulatedWaitMs);

        int dynamicMdbThreads = Math.min(MdbMetrics.getRate() / 2, 30);
        dto.setActiveMdbPoolCount(dynamicMdbThreads);
        dto.setMaxMdbPoolSize(60);

        return dto;
    }
}