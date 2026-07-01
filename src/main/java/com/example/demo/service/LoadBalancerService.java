package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LoadBalancerService {

    private final AtomicInteger counter = new AtomicInteger(0);

    private final List<String> servers = List.of(
            "http://localhost:8081",
            "http://localhost:8082",
            "http://localhost:8083"
    );

    public String getNextServer() {
        int index = counter.getAndIncrement() % servers.size();
        return servers.get(index);
    }
}