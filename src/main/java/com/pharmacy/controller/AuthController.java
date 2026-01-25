/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.security.JwtUtil;
import com.pharmacy.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final CustomUserDetailsService userDetailsService;
  private final JwtUtil jwtUtil;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), authRequest.getPassword()));
    UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
    String token = jwtUtil.generateToken(userDetails.getUsername());

    // Fetch user entity for user info
    com.pharmacy.entity.User user =
        userDetailsService.getUserEntityByUsername(authRequest.getUsername());
    UserInfo userInfo =
        new UserInfo(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole() != null ? user.getRole().getName() : null,
            user.getRole() != null
                ? user.getRole().getCreatedAt()
                : null // fallback to role createdAt if user createdAt not present
            );
    return ResponseEntity.ok(new AuthResponse(token, userInfo));
  }

  public static class AuthRequest {
    private String username;
    private String password;

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }
  }

  public static class AuthResponse {
    private String token;
    private UserInfo user;

    public AuthResponse(String token) {
      this.token = token;
    }

    public AuthResponse(String token, UserInfo user) {
      this.token = token;
      this.user = user;
    }

    public String getToken() {
      return token;
    }

    public void setToken(String token) {
      this.token = token;
    }

    public UserInfo getUser() {
      return user;
    }

    public void setUser(UserInfo user) {
      this.user = user;
    }
  }

  public static class UserInfo {
    private Long id;
    private String username;
    private String email;
    private String role;
    private java.time.LocalDateTime createdAt;

    public UserInfo(
        Long id, String username, String email, String role, java.time.LocalDateTime createdAt) {
      this.id = id;
      this.username = username;
      this.email = email;
      this.role = role;
      this.createdAt = createdAt;
    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getRole() {
      return role;
    }

    public void setRole(String role) {
      this.role = role;
    }

    public java.time.LocalDateTime getCreatedAt() {
      return createdAt;
    }

    public void setCreatedAt(java.time.LocalDateTime createdAt) {
      this.createdAt = createdAt;
    }
  }
}
