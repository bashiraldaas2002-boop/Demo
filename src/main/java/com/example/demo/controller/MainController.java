package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.service.*;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.OrderRequest;

@RestController
@RequestMapping("/products")
public class MainController {

    private final ProductService service;
    private final HeavyService heavyService;
    private final ProductBatchService productBatchService;
    private final OrderService orderService;
    private final TransactionService transactionService;

    public MainController(
            ProductService service,
            HeavyService heavyService,
            ProductBatchService productBatchService,
            OrderService orderService,
            TransactionService transactionService
) {
        this.service = service;
        this.heavyService = heavyService;
        this.productBatchService = productBatchService;
        this.orderService = orderService;
        this.transactionService = transactionService;
    }

    // Race Condition
    @PostMapping("/before-buy/{id}")
    public String beforeBuyProduct(@PathVariable Long id) throws Exception {
        return service.beforeBuyProduct(id);
    }

    @PostMapping("/after-buy/{id}")
    public String afterBuyProduct(@PathVariable Long id) throws Exception {
        return service.afterBuyProduct(id);
    }

    // Capacity Control
    @PostMapping("/before-heavy")
    public String beforeHeavy() throws Exception {
        heavyService.beforeHeavy();
        return "Heavy Task Done";
    }

    @PostMapping("/after-heavy")
    public String afterHeavy() throws Exception {
        heavyService.afterHeavy();
        return "Task Submitted Successfully";
    }

    // Async Queue
    @PostMapping("/before-order")
    public String beforeOrder() {
        return orderService.beforeOrder();
    }

    @PostMapping("/after-order")
    public String afterOrder() {
        orderService.afterOrder();
        return "Order Received (Processing in Background)";
    }

    // Batch Processing
    @PostMapping("/before-batch")
    public String beforeBatch() {
        productBatchService.beforeBatchUpdate();
        return "Before Batch Processing Done";
    }

    @PostMapping("/after-batch")
    public String afterBatch() {
        productBatchService.afterBatchUpdate();
        return "After Batch Processing Done";
    }

    // Loading
    @PostMapping("/before-load-balance")
    public String beforeLoadBalance(@RequestBody OrderRequest request) {
        orderService.beforeCreateOrder(request);
        return "Before Load Balancing Done";
    }

    @PostMapping("/after-load-balance")
    public String afterLoadBalance(@RequestBody OrderRequest request) {
        orderService.afterCreateOrder(request);
        return "After Load Balancing Done (Async + Distributed)";
    }

    // Caching
    @GetMapping("/before-cache/{id}")
    public Product beforeCache(@PathVariable Long id) {
        return service.beforeGetProduct(id);
    }

    @GetMapping("/after-cache/{id}")
    public Product afterCache(@PathVariable Long id) {
        return service.afterGetProduct(id);
    }

    // Concurrency Control (lock)
    @PostMapping("/before-lock/{id}")
    public String beforeLock(@PathVariable Long id) throws Exception {
        return service.beforeLock(id);
    }

    @PostMapping("/after-lock/{id}")
    public String afterLock(@PathVariable Long id) throws Exception {
        return service.afterLock(id);
    }

    // ACID transaction
    @PostMapping("/before-transaction/{id}")
    public String beforeTransaction(@PathVariable Long id) {
        return transactionService.beforeTransaction(id);
    }

    @PostMapping("/after-transaction/{id}")
    public String afterTransaction(@PathVariable Long id) {
        return transactionService.afterTransaction(id);
    }



    @org.springframework.beans.factory.annotation.Autowired
    private com.example.demo.service.MergedStressService mergedStressService;

    @PostMapping("/before-merged-stress")
    public String beforeMergedStress() throws Exception {
        return mergedStressService.executeBeforeStress();
    }

    @PostMapping("/after-merged-stress")
    public String afterMergedStress() throws Exception {
        mergedStressService.executeAfterStress();
        return "Task Distributed to Cluster";
    }

}