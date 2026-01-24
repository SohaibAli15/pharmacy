/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Role findByName(String name);
}
