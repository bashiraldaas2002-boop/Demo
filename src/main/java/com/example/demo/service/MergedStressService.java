package com.example.demo.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MergedStressService {

    private final AtomicInteger totalRequests = new AtomicInteger(0);
    private final AtomicInteger successRequests = new AtomicInteger(0);
    private final AtomicInteger failedRequests = new AtomicInteger(0);
    private final AtomicLong accumulatedTime = new AtomicLong(0);

    private volatile long lastRequestTime = System.currentTimeMillis();

    //  Before
    public String executeBeforeStress() {
        totalRequests.incrementAndGet();
        lastRequestTime = System.currentTimeMillis();
        long start = System.currentTimeMillis();
        try {
            List<byte[]> memoryHog = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                memoryHog.add(new byte[1024 * 1024]);
            }
            Thread.sleep(3000);
            successRequests.incrementAndGet();

            long end = System.currentTimeMillis();
            accumulatedTime.addAndGet(end - start);
            triggerPrintCheck("BEFORE");
            return "Success (Before)";
        } catch (Exception e) {
            failedRequests.incrementAndGet();
            triggerPrintCheck("BEFORE");
            return "Failed (Before)";
        }
    }

    //  After
    @Async("CustomExecutor")
    public void executeAfterStress() {
        totalRequests.incrementAndGet();
        lastRequestTime = System.currentTimeMillis();
        long start = System.currentTimeMillis();
        try {
            List<byte[]> memoryHog = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                memoryHog.add(new byte[1024 * 1024]);
            }
            Thread.sleep(3000);
            successRequests.incrementAndGet();
        } catch (Exception e) {
            failedRequests.incrementAndGet();
        } finally {
            long end = System.currentTimeMillis();
            accumulatedTime.addAndGet(end - start);
            triggerPrintCheck("AFTER (DISTRIBUTED)");
        }
    }

    private void triggerPrintCheck(String phase) {
        new Thread(() -> {
            try {
                Thread.sleep(2000);

                if (System.currentTimeMillis() - lastRequestTime >= 2000 && totalRequests.get() > 0) {
                    printFinalReport(phase);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private synchronized void printFinalReport(String phase) {
        long total = totalRequests.get();
        if (total == 0)
            return;

        long success = successRequests.get();
        long failed = failedRequests.get();
        long totalTime = accumulatedTime.get();
        long avgTime = total > 0 ? (totalTime / total) : 0;

        String port = System.getProperty("server.port", "8080/8081/8082");

        System.out.println("\n=============================================");
        System.out.println("=== REPORT | NODE PORT: " + port + " ===");
        System.out.println("=============================================");
        System.out.println("Phase Name: " + phase);
        System.out.println("Requests Processed on this Node: " + total);
        System.out.println("Success: " + success + " | Failed: " + failed);
        System.out.println("Average Response Time: " + avgTime + " ms");
        System.out.println("=============================================\n");

        totalRequests.set(0);
        successRequests.set(0);
        failedRequests.set(0);
        accumulatedTime.set(0);
    }
}