package com.example.demo.controller;

import com.example.demo.service.AppOrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class AppOrderController {

    private final AppOrderService orderService;

    public AppOrderController(AppOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/before-n1")
    public String beforeN1() {
        long startTime = System.currentTimeMillis();
        int count = orderService.getOrdersWithNPlusOne();
        long executionTime = System.currentTimeMillis() - startTime;

        System.out.println("==================================");
        System.out.println("Method: beforeN1 (N+1 Issue) | Execution Time: " + executionTime + " ms");
        System.out.println("==================================");

        return "Fetched " + count + " orders (N+1) in " + executionTime + " ms";
    }

    @PostMapping("/after-n1")
    public String afterN1() {
        long startTime = System.currentTimeMillis();
        int count = orderService.getOrdersOptimized();
        long executionTime = System.currentTimeMillis() - startTime;

        System.out.println("==================================");
        System.out.println("Method: afterN1 (Optimized) | Execution Time: " + executionTime + " ms");
        System.out.println("==================================");

        return "Fetched " + count + " orders optimized in " + executionTime + " ms";
    }
}