package com.roselyn.shoppe.repository;

import com.roselyn.shoppe.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByCategoryName(String name);

    Integer countByCategoryName(String name);
}
