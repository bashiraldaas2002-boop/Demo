package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.repository.OrderRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.example.demo.dto.OrderRequest;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class OrderService {

    private final LoadBalancerService loadBalancer;
    private final OrderRepository orderRepository;

    public OrderService(
            OrderRepository orderRepository, LoadBalancerService loadBalancer) {
        this.orderRepository = orderRepository;
        this.loadBalancer = loadBalancer;
    }

    public String beforeOrder() {
        System.out.println("=== BEFORE ORDER PROCESSING ===");
        List<Order> orders = orderRepository.findAll();
        double total = 0;
        for (Order order : orders) {
            total += order.getPrice() * order.getQuantity();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Total Value: " + total);
        return "Total Orders Value: " + total;
    }

    @Async
    public void afterOrder() {
        System.out.println("=== AFTER ORDER PROCESSING (ASYNC) ===");
        System.out.println("Thread: " + Thread.currentThread().getName());
        List<Order> orders = orderRepository.findAll();
        double total = 0;
        for (Order order : orders) {
            total += order.getPrice() * order.getQuantity();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Async Order Processing Completed");
        System.out.println("Total Value: " + total);
    }

    public void beforeCreateOrder(OrderRequest request) {
        System.out.println("========= BEFORE LOAD BALANCING =========");
        System.out.println("Thread: " + Thread.currentThread().getName());
        Order order = new Order();
        order.setProductName(request.getProductName());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());
        orderRepository.save(order);
        System.out.println("Order saved (SINGLE THREAD)");
    }

    public void afterCreateOrder(OrderRequest request) {
        String server = loadBalancer.getNextServer();
        System.out.println("==================================");
        System.out.println("========= AFTER LOAD BALANCING =========");
        System.out.println("Server : " + server);
        System.out.println("Thread : " + Thread.currentThread().getName());
        System.out.println("Product : " + request.getProductName());
        System.out.println("==================================");
        Order order = new Order();
        order.setProductName(request.getProductName());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());
        orderRepository.save(order);
        System.out.println("Order saved successfully");
    }
}
