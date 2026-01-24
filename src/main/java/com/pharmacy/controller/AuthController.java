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
    return ResponseEntity.ok(new AuthResponse(token));
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

    public AuthResponse(String token) {
      this.token = token;
    }

    public String getToken() {
      return token;
    }

    public void setToken(String token) {
      this.token = token;
    }
  }
}
