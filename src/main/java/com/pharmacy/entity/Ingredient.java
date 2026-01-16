/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private String unit; // e.g., kg, liters, pieces

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal currentStock;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal thresholdLow;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal thresholdHigh;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal costPerUnit;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;
}
