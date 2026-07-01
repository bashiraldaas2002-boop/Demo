package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductBatchService {

    private final ProductRepository productRepository;

    public ProductBatchService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void beforeBatchUpdate() {
        System.out.println("=== BEFORE BATCH PROCESSING ===");
        List<Product> products = productRepository.findAll();
        for (Product p : products) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            p.setStock((int) (p.getStock() * 1.1));
            productRepository.save(p);
        }
        System.out.println("Before Batch Processing Completed");
    }

    public void afterBatchUpdate() {
        System.out.println("=== AFTER BATCH PROCESSING ===");
        int page = 0;
        int size = 100;
        Page<Product> productPage;
        do {
            productPage = productRepository.findAll(PageRequest.of(page, size));
            List<Product> products =
                    productPage.getContent();
            for (Product p : products) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                p.setStock((int) (p.getStock() * 1.1));
            }
            productRepository.saveAll(products);
            System.out.println(
                    "Processed Batch Page: " + page
            );
            page++;
        } while (productPage.hasNext());
        System.out.println("After Batch Processing Completed");
    }
}