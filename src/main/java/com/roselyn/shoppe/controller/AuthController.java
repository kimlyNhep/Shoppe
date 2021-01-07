package com.roselyn.shoppe.controller;

import com.roselyn.shoppe.config.JwtTokenUtil;
import com.roselyn.shoppe.exception.SpAuthException;
import com.roselyn.shoppe.model.User;
import com.roselyn.shoppe.service.AuthService;
import com.roselyn.shoppe.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class AuthController {

    @Autowired
    AuthService authService;
    @Autowired
    RoleService roleService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, Object> userMap) {
        String firstName = (String) userMap.get("firstName");
        String lastName = (String) userMap.get("lastName");
        String username = (String) userMap.get("username");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");

        UserDetails user = authService.registerUser(new User(firstName, lastName, username, email, password));
        final String token = jwtTokenUtil.generateToken(user);

        Map<String, String> map = new HashMap<>();
        map.put("token", token);

        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, Object> userMap) throws Exception {
        String username = (String) userMap.get("username");
        String password = (String) userMap.get("password");

        authenticate(username, password);
        final UserDetails userDetails = authService.loadUserByUsername(username);
        final String token = jwtTokenUtil.generateToken(userDetails);

        Map<String, String> map = new HashMap<>();
        map.put("token", token);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, User>> getProfile(HttpServletRequest request) throws SpAuthException {
        try {
            final String requestTokenHeader = request.getHeader("Authorization");
            String token = requestTokenHeader.substring(7);
            User userToken = jwtTokenUtil.getUserFromToken(token);

            Integer id = (Integer) userToken.getId();

            User user = authService.getMeUser(id);
            Map<String, User> map = new HashMap<>();
            map.put("user", user);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            throw new SpAuthException("Unauthorized");
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
