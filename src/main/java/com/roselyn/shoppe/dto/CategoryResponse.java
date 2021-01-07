package com.roselyn.shoppe.dto;

import com.roselyn.shoppe.model.Product;
import com.roselyn.shoppe.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryResponse {
    private Integer id;
    private String categoryName;
    private User user;
    private List<Product> products;

    public CategoryResponse(Integer id, String categoryName, User user, List<Product> products) {
        this.id = id;
        this.categoryName = categoryName;
        this.setUser(user);
        this.setProducts(products);
    }

    public CategoryResponse(Integer id, User user, List<Product> products) {
        this.id = id;
        this.setUser(user);
        this.setProducts(products);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = new User(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getRoles());
    }

    public List<Product> getProducts() {
        return products.stream().map(product -> new Product(product.getId(), product.getProductName(), product.getDescription(), product.getPrice(),
                product.getCategory())).collect(Collectors.toList());
    }

    public void setProducts(List<Product> products) {
//        this.products = products.stream().map(product -> new ProductResponse(product.getId())).collect(Collectors.toList());
        this.products = products;
    }
}
