package com.roselyn.shoppe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer Id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles", joinColumns = {
            @JoinColumn(name = "user_id")
    }, inverseJoinColumns = {
            @JoinColumn(name = "role_id")
    })
    private List<Role> roles = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Category> categories = new ArrayList<>();

    public User(Integer Id, String first_name, String last_name, String username, String email, String password) {
        this.Id = Id;
        this.firstName = first_name;
        this.lastName = last_name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String first_name, String last_name, String username, String email, String password) {
        this.firstName = first_name;
        this.lastName = last_name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(Integer Id, String first_name, String last_name, String username) {
        this.Id = Id;
        this.firstName = first_name;
        this.lastName = last_name;
        this.username = username;
    }

    public User(Integer Id, String first_name, String last_name, String username, String email, List<Role> roles) {
        this.Id = Id;
        this.firstName = first_name;
        this.lastName = last_name;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
