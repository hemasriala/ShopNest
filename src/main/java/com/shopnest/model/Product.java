package com.shopnest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter @Setter @NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 1000)
    private String description;

    private double price;   // selling price in INR
    private double mrp;     // original price in INR
    private String category;
    private int stock;

    @Column(length = 500)
    private String imageUrl;

    public Product(String name, String description, double price, double mrp,
                   String category, int stock, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.mrp = mrp;
        this.category = category;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    public int getDiscountPercent() {
        if (mrp <= price || mrp == 0) return 0;
        return (int) Math.round((mrp - price) / mrp * 100);
    }
}
