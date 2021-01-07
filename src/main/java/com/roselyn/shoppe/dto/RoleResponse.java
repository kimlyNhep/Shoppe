package com.roselyn.shoppe.dto;

import com.roselyn.shoppe.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class RoleResponse {
    private Integer roleId;
    private String roleName;
    private List<UserResponse> users;

    public RoleResponse() {

    }

    public RoleResponse(Integer roleId, String roleName, List<User> users) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.setUsers(users);
    }

    public RoleResponse(Integer roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<UserResponse> getUsers() {
        return users.stream().map(user -> new UserResponse(user.getId(), user.getFirstName(), user.getLastName(),
                user.getUsername())).collect(Collectors.toList());
    }

    public void setUsers(List<User> users) {
        this.users = users.stream().map(user -> new UserResponse(user.getId(), user.getFirstName(), user.getLastName(),
                user.getUsername())).collect(Collectors.toList());
    }
}
