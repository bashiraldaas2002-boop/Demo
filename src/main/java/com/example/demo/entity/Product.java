package com.example.demo.entity;
import java.io.Serializable;
import jakarta.persistence.*;
import jakarta.persistence.GenerationType;
import lombok.Data;

@Entity
@Data
public class Product implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int stock;
}
