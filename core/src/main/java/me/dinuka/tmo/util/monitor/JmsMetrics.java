package me.dinuka.tmo.util.monitor;

import java.util.concurrent.atomic.AtomicInteger;

public class JmsMetrics {

    private static final AtomicInteger totalMessagesSent = new AtomicInteger(0);
    private static final AtomicInteger totalMessagesReceived = new AtomicInteger(0);
    private static final AtomicInteger failedMessages = new AtomicInteger(0);

    // Call this when a JMS message is sent
    public static void messageSent() {
        totalMessagesSent.incrementAndGet();
    }

    // Call this when a JMS message is received/consumed
    public static void messageReceived() {
        totalMessagesReceived.incrementAndGet();
    }

    // Call this when a JMS message fails
    public static void messageFailed() {
        failedMessages.incrementAndGet();
    }

    public static int getTotalMessages() {
        return totalMessagesSent.get();
    }

    public static int getTotalMessagesReceived() {
        return totalMessagesReceived.get();
    }

    public static int getFailedMessages() {
        return failedMessages.get();
    }

    public static void reset() {
        totalMessagesSent.set(0);
        totalMessagesReceived.set(0);
        failedMessages.set(0);
    }
}
