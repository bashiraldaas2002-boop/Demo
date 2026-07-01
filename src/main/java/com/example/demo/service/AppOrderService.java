package com.example.demo.service;

import com.example.demo.entity.AppOrder;
import com.example.demo.repository.AppOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AppOrderService {

    private final AppOrderRepository orderRepository;

    public AppOrderService(AppOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Before (N+1 Query)
    @Transactional(readOnly = true)
    public int getOrdersWithNPlusOne() {
        List<AppOrder> orders = orderRepository.findAll();

        int dummyCounter = 0;
        for (AppOrder order : orders) {
            if (order.getUser() != null) {
                dummyCounter += order.getUser().getUsername().length();
            }
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return orders.size();
    }

    // After (Optimized with JOIN FETCH)
    @Transactional(readOnly = true)
    public int getOrdersOptimized() {
        List<AppOrder> orders = orderRepository.findAllWithUserFetch();

        int dummyCounter = 0;
        for (AppOrder order : orders) {
            if (order.getUser() != null) {
                dummyCounter += order.getUser().getUsername().length();
            }
        }
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return orders.size();
    }
}