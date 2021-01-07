package com.roselyn.shoppe.repository;

import com.roselyn.shoppe.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Integer countByProductName(String name);
}
