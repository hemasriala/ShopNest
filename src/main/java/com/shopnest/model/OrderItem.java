package com.shopnest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_items")
@Getter @Setter @NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;

    private Long productId;

    private String productName;

    @Column(length = 500)
    private String imageUrl;

    private double price;
    private int quantity;

    public double getLineTotal() {
        return price * quantity;
    }
}
