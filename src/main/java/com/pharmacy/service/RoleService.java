/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pharmacy.entity.Role;
import com.pharmacy.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {
  private final RoleRepository roleRepository;

  public List<Role> getAllRoles() {
    return roleRepository.findAll();
  }

  public Optional<Role> getRoleById(Long id) {
    return roleRepository.findById(id);
  }

  public Role createRole(Role role) {
    return roleRepository.save(role);
  }

  public Role updateRole(Long id, Role role) {
    role.setId(id);
    return roleRepository.save(role);
  }

  public void deleteRole(Long id) {
    roleRepository.deleteById(id);
  }
}
