package com.roselyn.shoppe.controller;

import com.roselyn.shoppe.dto.RoleResponse;
import com.roselyn.shoppe.dto.UserResponse;
import com.roselyn.shoppe.exception.SpAuthException;
import com.roselyn.shoppe.model.Role;
import com.roselyn.shoppe.service.AuthService;
import com.roselyn.shoppe.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    RoleService roleService;

    @Autowired
    AuthService authService;


    @PostMapping("")
    public ResponseEntity<Map<String, String>> createRole(HttpServletRequest request, @RequestBody Map<String, Object> roleMap)
            throws SpAuthException {
        String roleName = (String) roleMap.get("roleName");

        Role role = roleService.createRole(new Role(roleName));
        Map<String, String> map = new HashMap<>();
        map.put("Successful", role.getName());
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<String> changeRole(HttpServletRequest request, @RequestBody Map<String, Object> roleMap,
                                             @PathVariable("userId") Integer userId) throws SpAuthException {
        ArrayList<Integer> roles = (ArrayList<Integer>) roleMap.get("roles");
        authService.updateRole(userId, roles);
        return new ResponseEntity<>("Update Successful", HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<RoleResponse>> getRoles(HttpServletRequest request) {
        List<RoleResponse> roles = roleService.getRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<UserResponse> users = authService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Role>> getRolesByUser(@PathVariable("userId") Integer userId) {
        List<Role> roles = roleService.getRolesByUserId(userId);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}
