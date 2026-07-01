package com.example.demo.dto;

import lombok.Data;

@Data
public class OrderRequest {

    private String productName;
    private int quantity;
    private double price;
}
