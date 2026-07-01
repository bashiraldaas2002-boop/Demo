package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ServerPoolConfig {

    @Bean("server2")
    public ExecutorService server2() {
        return Executors.newFixedThreadPool(2);
    }

    @Bean("server3")
    public ExecutorService server3() {
        return Executors.newFixedThreadPool(2);
    }

    @Bean("server4")
    public ExecutorService server4() {
        return Executors.newFixedThreadPool(2);
    }
}