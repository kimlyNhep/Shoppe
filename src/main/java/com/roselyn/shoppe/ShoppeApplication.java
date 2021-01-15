package com.roselyn.shoppe;

import com.roselyn.shoppe.model.Role;
import com.roselyn.shoppe.repository.RoleRepository;
import com.roselyn.shoppe.service.RoleService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ShoppeApplication {
    @Autowired
    RoleService roleService;

    public static void main(String[] args) {
        SpringApplication.run(ShoppeApplication.class, args);
    }
}

