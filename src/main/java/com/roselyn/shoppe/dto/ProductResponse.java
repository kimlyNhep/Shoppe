package com.roselyn.shoppe.dto;

import com.roselyn.shoppe.model.Category;

public class ProductResponse {
    private Integer id;
    private String productName;
    private String description;
    private Double price;
    private CategoryResponse category;

    public ProductResponse(Integer id, String productName, String description, Double price, Category category) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.setCategory(category);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public CategoryResponse getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = new CategoryResponse(category.getId(), category.getUser(), category.getProducts());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
