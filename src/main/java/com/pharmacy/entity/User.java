/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.entity;

import jakarta.persistence.*;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(unique = true, nullable = false)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Schema(
      description = "Role of the user. Possible values: ADMIN, PHARMACIST, CUSTOMER",
      example = "ADMIN")
  private Role role;

  public enum Role {
    ADMIN,
    PHARMACIST,
    CUSTOMER
  }
}
