package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public String beforeBuyProduct(Long id) throws Exception {
        System.out.println("=== BEFORE BUY (NO LOCK) ===");
        Product product = productRepository.findById(id).orElseThrow();
        if (product.getStock() <= 0) {
            return "Out of stock";
        }
        Thread.sleep(3000);
        product.setStock(product.getStock() - 1);
        productRepository.save(product);
        return "Purchased Successfully (NO LOCK)";
    }

    @Transactional
    public String afterBuyProduct(Long id) throws Exception {
        System.out.println("=== AFTER BUY (WITH LOCK) ===");
        Product product = productRepository.findByIdWithLock(id);
        if (product.getStock() <= 0) {
            return "Out of stock";
        }
        Thread.sleep(3000);
        product.setStock(product.getStock() - 1);
        productRepository.save(product);
        return "Purchased Successfully (WITH LOCK)";
    }


    public Product beforeGetProduct(Long id) {
        System.out.println("=== BEFORE CACHE ===");
        Product product = productRepository.findById(id).orElseThrow();
        System.out.println("Fetched from DATABASE");
        return product;
    }

    @Cacheable(value = "products", key = "#id")
    public Product afterGetProduct(Long id) {
        System.out.println("=== AFTER CACHE ===");
        Product product = productRepository.findById(id).orElseThrow();
        System.out.println("Fetched from DATABASE + stored in CACHE");

        return product;
    }


    public String beforeLock(Long id) throws Exception {
        System.out.println("=== BEFORE LOCK ===");
        System.out.println("Thread: " + Thread.currentThread().getName());
        Product product = productRepository.findById(id).orElseThrow();
        Thread.sleep(5000);
        product.setStock(product.getStock() - 1);
        productRepository.save(product);
        return "Updated WITHOUT LOCK";
    }

    @Transactional
    public String afterLock(Long id) throws Exception {
        System.out.println("=== AFTER LOCK ===");
        System.out.println("Thread: " + Thread.currentThread().getName());
        Product product = productRepository.findByIdWithLock(id);
        Thread.sleep(5000);
        product.setStock(product.getStock() - 1);
        productRepository.save(product);
        return "Updated WITH LOCK";
    }
}