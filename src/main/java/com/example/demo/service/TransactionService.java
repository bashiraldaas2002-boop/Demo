package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Data
public class TransactionService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public TransactionService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public String beforeTransaction(Long productId) {
        System.out.println("=== BEFORE TRANSACTION (NO ACID) ===");
        Product product = productRepository.findById(productId).orElseThrow();
        product.setStock(product.getStock() - 1);
        productRepository.save(product);
        System.out.println("Product updated");
        int x = 10 / 0;
        Order order = new Order();
        order.setProductName(product.getName());
        order.setQuantity(1);
        orderRepository.save(order);
        return "Done";
    }

    @Transactional
    public String afterTransaction(Long productId) {
        System.out.println("=== AFTER TRANSACTION (ACID ENABLED) ===");
        Product product = productRepository.findById(productId).orElseThrow();
        product.setStock(product.getStock() - 1);
        productRepository.save(product);
        System.out.println("Product updated");
        int x = 10 / 0;
        Order order = new Order();
        order.setProductName(product.getName());
        order.setQuantity(1);
        orderRepository.save(order);
        return "Done";
    }
}
