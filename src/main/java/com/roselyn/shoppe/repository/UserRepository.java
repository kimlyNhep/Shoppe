package com.roselyn.shoppe.repository;

import com.roselyn.shoppe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Integer countByEmail(String email);

    Integer countByUsername(String username);

    User findByEmail(String email);

    User findByUsername(String username);
}
