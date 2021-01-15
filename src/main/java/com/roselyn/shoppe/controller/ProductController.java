package com.roselyn.shoppe.controller;

import com.roselyn.shoppe.model.Category;
import com.roselyn.shoppe.model.Product;
import com.roselyn.shoppe.service.CategoryService;
import com.roselyn.shoppe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<Map<String, Integer>> createProduct(@RequestBody Map<String, Object> productMap) {
        String productName = (String) productMap.get("productName");
        String description = (String) productMap.get("description");
        Double price = Double.parseDouble(productMap.get("price").toString());
        Integer categoryId = (Integer) productMap.get("categoryId");

        Integer productId = productService.createProduct(new Product(productName, description, price), categoryId);
        Map<String, Integer> map = new HashMap<>();
        map.put("productId", productId);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Map<String, Product>> updateProduct(@PathVariable("productId") Integer productId, @RequestBody Map<String, Object> productMap) {
        String productName = (String) productMap.get("productName");
        String description = (String) productMap.get("description");
        Double price = (Double) productMap.get("price");
        Integer categoryId = (Integer) productMap.get("categoryId");

        Category category = categoryService.getCategory(categoryId);

        Product product = productService.updateProduct(new Product(productName, description, price, category), productId);
        Map<String, Product> map = new HashMap<>();
        map.put("product", product);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Map<String, Product>> getProduct(@PathVariable("productId") Integer productId) {
        Product product = productService.getProduct(productId);
        Map<String, Product> map = new HashMap<>();
        map.put("product", product);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = productService.getProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Integer productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>("Delete Successful", HttpStatus.NO_CONTENT);
    }
}
