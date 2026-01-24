/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.entity.Role;
import com.pharmacy.service.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RoleController {
  private final RoleService roleService;

  @GetMapping
  public ResponseEntity<List<Role>> getAllRoles() {
    return ResponseEntity.ok(roleService.getAllRoles());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
    return roleService
        .getRoleById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Role> createRole(@RequestBody Role role) {
    return ResponseEntity.ok(roleService.createRole(role));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
    return ResponseEntity.ok(roleService.updateRole(id, role));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
    roleService.deleteRole(id);
    return ResponseEntity.noContent().build();
  }
}
