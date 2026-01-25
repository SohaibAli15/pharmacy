/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pharmacy.dto.UserDto;
import com.pharmacy.entity.Role;
import com.pharmacy.entity.User;
import com.pharmacy.repository.RoleRepository;
import com.pharmacy.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;

  public User saveUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  public User saveUser(UserDto userDto) {
    User user = new User();
    user.setUsername(userDto.getUsername());
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    user.setEmail(userDto.getEmail());
    if (userDto.getRoleId() != null) {
      Role role =
          roleRepository
              .findById(userDto.getRoleId())
              .orElseThrow(() -> new RuntimeException("Role not found"));
      user.setRole(role);
    }
    return userRepository.save(user);
  }

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
}
