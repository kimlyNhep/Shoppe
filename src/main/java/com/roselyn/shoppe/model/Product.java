package com.roselyn.shoppe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer Id;
    private String productName;
    private String description;
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Product(String productName, String description, Double price) {
        this.productName = productName;
        this.description = description;
        this.price = price;
    }

    public Product(String productName, String description, Double price, Category category) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.category = category;
    }
}
