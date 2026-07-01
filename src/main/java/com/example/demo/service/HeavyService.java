package com.example.demo.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class HeavyService {

    public void beforeHeavy() throws Exception {
        System.out.println("=== BEFORE HEAVY (SYNC) ===");
        System.out.println("Thread: " + Thread.currentThread().getName());
        Thread.sleep(3000);
        System.out.println("Heavy Task Completed");
    }

    @Async
    public void afterHeavy() throws Exception {
        System.out.println("=== AFTER HEAVY (ASYNC) ===");
        System.out.println("Thread: " + Thread.currentThread().getName());
        Thread.sleep(3000);
        System.out.println("Async Heavy Task Completed");
    }
}