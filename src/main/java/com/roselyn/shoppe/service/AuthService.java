package com.roselyn.shoppe.service;

import com.roselyn.shoppe.dto.UserResponse;
import com.roselyn.shoppe.exception.SpAuthException;
import com.roselyn.shoppe.exception.SpResourceNotFoundException;
import com.roselyn.shoppe.model.Role;
import com.roselyn.shoppe.model.User;
import com.roselyn.shoppe.repository.RoleRepository;
import com.roselyn.shoppe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDetails registerUser(User user) throws SpAuthException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        String email = user.getEmail();

        if (email != null) {
            email = email.toLowerCase();
            user.setEmail(email);
        }

        assert email != null;
        if (!pattern.matcher(email).matches())
            throw new SpAuthException("Invalid Email Format");

        Integer count = userRepository.countByEmail(email);
        if (count > 0) throw new SpAuthException("Email already exist");

        Integer countUsername = userRepository.countByUsername(user.getUsername());
        if (countUsername > 0) throw new SpAuthException("Username already exist");

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        // find role by name -- by default set role to user
        Role userRole = roleRepository.findByName("USER");
        if (userRole == null) throw new SpResourceNotFoundException("Role Not Found");

        user.getRoles().add(userRole);
        userRole.getUsers().add(user);

        return loadUserByUsername(userRepository.save(user).getUsername());
    }

    public void updateRole(Integer userId, ArrayList<Integer> roleIds) throws SpResourceNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) throw new SpResourceNotFoundException("User Not Found");
        user.get().getRoles().clear();
        for (Integer roleId : roleIds) {
            Optional<Role> role = roleRepository.findById(roleId);
            if (role.isEmpty())
                throw new SpResourceNotFoundException("role Not Found");
            user.get().getRoles().add(role.get());
        }
    }

    public User getMeUser(Integer userId) throws SpResourceNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) throw new SpResourceNotFoundException("User Not Found");
        return user.map(u -> new User(u.getId(), u.getFirstName(), u.getLastName(), u.getUsername(), u.getEmail(),
                u.getRoles().stream().map(role -> new Role(role.getId(), role.getName())).collect(Collectors.toList()))).get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) throw new UsernameNotFoundException("User not found");
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                getAuthorities(user.getRoles())
        );
    }

    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> new UserResponse(user.getId(), user.getFirstName(), user.getLastName(),
                user.getUsername(), user.getRoles())).collect(Collectors.toList());
    }


    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {
        List<GrantedAuthority> authorities
                = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authorities;
    }
}
