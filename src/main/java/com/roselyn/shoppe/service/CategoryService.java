package com.roselyn.shoppe.service;

import com.roselyn.shoppe.exception.SpBadRequestException;
import com.roselyn.shoppe.exception.SpResourceNotFoundException;
import com.roselyn.shoppe.model.Category;
import com.roselyn.shoppe.model.User;
import com.roselyn.shoppe.repository.CategoryRepository;
import com.roselyn.shoppe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    public Category returnCategory(Optional<Category> categoryOptional) {
        Optional<Category> existingCategory = categoryOptional.stream()
                .map(category -> new Category(category.getId(), category.getCategoryName(),
                        new User(category.getUser().getId(), category.getUser().getFirstName(), category.getUser().getLastName(),
                                category.getUser().getUsername()))).findFirst();
        assert existingCategory.isPresent();
        return existingCategory.get();
    }

    public Integer createCategory(String categoryName, Integer userId) throws SpBadRequestException, SpResourceNotFoundException {
        Integer count = categoryRepository.countByCategoryName(categoryName);
        if (count > 0) throw new SpBadRequestException("Category already exist");

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) throw new SpResourceNotFoundException("User Not Found");

        Category category = new Category(categoryName);
        user.get().getCategories().add(category);
        category.setUser(user.get());

        return categoryRepository.save(category).getId();
    }

    public List<Category> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(category ->
                new Category(category.getId(), category.getCategoryName(),
                        new User(category.getUser().getId(), category.getUser().getFirstName(), category.getUser().getLastName(),
                                category.getUser().getUsername()))).collect(Collectors.toList());
//        return categories.stream().map(category -> new CategoryResponse(category.getId(), category.getCategoryName(), category.getUser(), category.getProducts())).collect(Collectors.toList());
    }

    public Category getCategory(Integer categoryId) {
        try {
            Optional<Category> categoryMap = categoryRepository.findById(categoryId);
            return returnCategory(categoryMap);
        } catch (Exception e) {
            throw new SpResourceNotFoundException("Category Not Found");
        }
    }

    public Category updateCategory(Integer categoryId, String categoryName) {
        Optional<Category> existingCategory = categoryRepository.findById(categoryId);
        if (existingCategory.isEmpty()) throw new SpResourceNotFoundException("Category Not Found");
        Integer count = categoryRepository.countByCategoryName(categoryName);
        if (count > 0 && !existingCategory.get().getCategoryName().equals(categoryName))
            throw new SpBadRequestException("Category already exist");
        existingCategory.get().setCategoryName(categoryName);
        return this.returnCategory(existingCategory);
    }

    public void deleteCategory(Integer categoryId) throws SpBadRequestException, SpResourceNotFoundException {
        Optional<Category> existingCategory = categoryRepository.findById(categoryId);
        if (existingCategory.isEmpty()) throw new SpResourceNotFoundException("Category Not Found");
        try {
            categoryRepository.delete(existingCategory.get());
        } catch (Exception e) {
            throw new SpBadRequestException("This category is used!");
        }
    }
}
