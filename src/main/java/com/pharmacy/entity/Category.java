/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.entity;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 100)
  private String name;

  @Column(length = 255)
  private String description;

  @Column(nullable = false)
  private Boolean isActive = true;

  @Column(nullable = false, updatable = false)
  private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();

  @Column(nullable = false)
  private java.time.LocalDateTime updatedAt = java.time.LocalDateTime.now();
}
