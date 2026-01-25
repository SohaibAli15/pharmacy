/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.pharmacy.security.JwtAuthenticationFilter;
import com.pharmacy.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomUserDetailsService customUserDetailsService;

  @Autowired private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider =
        new DaoAuthenticationProvider(customUserDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(
      org.springframework.security.config.annotation.authentication.configuration
              .AuthenticationConfiguration
          configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  // Remove or comment out the temporary SecurityFilterChain
  // @Bean
  // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  //   http.csrf(AbstractHttpConfigurer::disable)
  //       .authorizeHttpRequests(
  //           authz ->
  //               authz
  //                   .requestMatchers("/api/v1/auth/login", "/swagger-ui/**",
  // "/swagger-ui.html").permitAll()
  //                   .anyRequest().authenticated())
  //       .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
  //   return http.build();
  // }

  // Enable production configuration for role-based authorization
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            authz ->
                authz
                    .requestMatchers("/api/v1/auth/login", "/swagger-ui/**", "/swagger-ui.html")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .httpBasic(httpBasic -> {})
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/api/**")
                    .allowedOriginPatterns("*")  // Changed from allowedOrigins
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                    .allowedHeaders("*")
                    .allowCredentials(true)  // Added this
                    .maxAge(3600);
        }
    };
  }
}
