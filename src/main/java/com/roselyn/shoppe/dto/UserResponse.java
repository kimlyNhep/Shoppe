package com.roselyn.shoppe.dto;

import com.roselyn.shoppe.model.Category;
import com.roselyn.shoppe.model.Role;

import java.util.List;
import java.util.stream.Collectors;

public class UserResponse {
    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private List<Role> roles;
    private List<Category> categories;

    public UserResponse() {

    }

    public UserResponse(Integer userId, String firstName, String lastName, String username, List<Role> roles,
                        List<Category> categories) {
        this.id = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.setRoles(roles);
        this.setCategories(categories);
    }

    public UserResponse(Integer userId, String firstName, String lastName, String username, List<Role> roles) {
        this.id = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.setRoles(roles);
    }

    public UserResponse(Integer userId, String firstName, String lastName, String username) {
        this.id = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles.stream().map(role ->
                new Role(role.getId(), role.getName())
        ).collect(Collectors.toList());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories.stream().map(category -> new Category(category.getId(), category.getCategoryName())).collect(Collectors.toList());
    }
}
