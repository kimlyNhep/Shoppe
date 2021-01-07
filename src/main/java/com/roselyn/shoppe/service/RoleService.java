package com.roselyn.shoppe.service;

import com.roselyn.shoppe.dto.RoleResponse;
import com.roselyn.shoppe.exception.SpBadRequestException;
import com.roselyn.shoppe.exception.SpResourceNotFoundException;
import com.roselyn.shoppe.model.Role;
import com.roselyn.shoppe.model.User;
import com.roselyn.shoppe.repository.RoleRepository;
import com.roselyn.shoppe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    public Role createRole(Role role) throws SpResourceNotFoundException {
        Integer count = roleRepository.countByName(role.getName());
        if (count > 0) throw new SpBadRequestException("Role already exist");

        Integer roleId = roleRepository.save(role).getId();
        return roleRepository.findById(roleId).orElseThrow();
    }

    public List<Role> getRolesByUserId(Integer uid) throws SpResourceNotFoundException {
        List<Role> roles;
        Optional<User> user = userRepository.findById(uid);
        if (user.isPresent())
            roles = user.get().getRoles();
        else throw new SpResourceNotFoundException("User Not Found");
        return roles.stream().map(role -> new Role(role.getId(), role.getName())).collect(Collectors.toList());
    }

    public List<RoleResponse> getRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(role -> new RoleResponse(role.getId(), role.getName(), role.getUsers())).collect(Collectors.toList());
    }
}
