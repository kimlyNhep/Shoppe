package com.roselyn.shoppe.controller;

import com.roselyn.shoppe.config.JwtTokenUtil;
import com.roselyn.shoppe.exception.SpBadRequestException;
import com.roselyn.shoppe.exception.SpResourceNotFoundException;
import com.roselyn.shoppe.model.Category;
import com.roselyn.shoppe.model.User;
import com.roselyn.shoppe.service.CategoryService;
import com.roselyn.shoppe.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    RoleService roleService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @PostMapping("")
    public ResponseEntity<Map<String, Integer>> createCategory(HttpServletRequest request, @RequestBody Map<String, Object> categoryMap) {
        final String requestTokenHeader = request.getHeader("Authorization");
        String token = requestTokenHeader.substring(7);
        User user = jwtTokenUtil.getUserFromToken(token);

        Integer id = (Integer) user.getId();
        String name = (String) categoryMap.get("categoryName");

        Integer categoryId = categoryService.createCategory(name, id);
        Map<String, Integer> map = new HashMap<>();
        map.put("id", categoryId);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<Category>> getCategories(HttpServletRequest request) {
        List<Category> categories = categoryService.getCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Map<String, Category>> getCategory(@PathVariable("categoryId") Integer categoryId) {
        Category category = categoryService.getCategory(categoryId);
        Map<String, Category> map = new HashMap<>();
        map.put("category", category);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Map<String, Category>> updateCategory(@PathVariable("categoryId") Integer categoryId,
                                                                @RequestBody Map<String, Object> categoryMap, HttpServletRequest request) {
        String categoryName = (String) categoryMap.get("categoryName");
        Category category = categoryService.updateCategory(categoryId, categoryName);
        Map<String, Category> map = new HashMap<>();
        map.put("category", category);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable("categoryId") Integer categoryId, HttpServletRequest request) throws SpResourceNotFoundException, SpBadRequestException {
        try {
            categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>("Delete Successful", HttpStatus.NO_CONTENT);
        } catch (SpResourceNotFoundException e) {
            throw new SpResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("Error", "This category is used");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }
}
