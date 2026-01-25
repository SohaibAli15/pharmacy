/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.Collections;
import java.util.List;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pharmacy.entity.User;
import com.pharmacy.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    List<GrantedAuthority> authorities =
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));

    return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
        .password(user.getPassword())
        .authorities(authorities)
        .accountExpired(false)
        .accountLocked(false)
        .credentialsExpired(false)
        .disabled(false)
        .build();
  }

  public User getUserEntityByUsername(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
  }
}
