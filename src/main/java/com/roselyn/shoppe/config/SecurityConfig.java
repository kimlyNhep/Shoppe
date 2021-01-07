package com.roselyn.shoppe.config;

import com.roselyn.shoppe.filter.JwtRequestFilter;
import com.roselyn.shoppe.service.AuthService;
import com.roselyn.shoppe.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthService authService;
    @Autowired
    ShoppeAuthenticationEntryPoint shoppeAuthenticationEntryPoint;
    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @Autowired
    RoleService roleService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/users/login", "/api/users/register").permitAll()
                .antMatchers(HttpMethod.GET, "/api/categories/**").hasAnyAuthority("STAFF", "ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/api/categories/**").hasAnyAuthority("STAFF", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/categories/**").hasAnyAuthority("STAFF", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/categories/**").hasAnyAuthority("STAFF", "ADMIN")
                .antMatchers(HttpMethod.GET, "/api/products/**").hasAnyAuthority("USER", "STAFF", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/products/**").hasAnyAuthority("STAFF", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/products/**").hasAnyAuthority("STAFF", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/products/**").hasAnyAuthority("STAFF", "ADMIN")
                .antMatchers("/api/roles/**").hasAnyAuthority("ADMIN")
                .anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(shoppeAuthenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // configure AuthenticationManager so that it knows from where to load
        // user for matching credentials
        // Use BCryptPasswordEncoder
        auth.userDetailsService(authService).passwordEncoder(encoder());
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
