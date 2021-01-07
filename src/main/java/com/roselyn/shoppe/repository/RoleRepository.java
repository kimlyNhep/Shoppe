package com.roselyn.shoppe.repository;

import com.roselyn.shoppe.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Integer countByName(String name);

    Role findByName(String name);
}
