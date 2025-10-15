package com.medicconnect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disable CSRF for APIs
            .authorizeHttpRequests()
                .requestMatchers("/api/**").permitAll() // Allow all API endpoints
                .anyRequest().authenticated() // Just in case, other requests still need auth
            .and()
            .httpBasic().disable(); // Disable login page / basic auth

        return http.build();
    }
}
