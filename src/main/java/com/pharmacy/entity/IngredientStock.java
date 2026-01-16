/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "ingredient_stock",
    uniqueConstraints = {
      @UniqueConstraint(columnNames = {"store_id", "ingredient_id", "batch_number"})
    })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientStock {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "store_id", nullable = false)
  private Store store;

  @ManyToOne
  @JoinColumn(name = "ingredient_id", nullable = false)
  private Ingredient ingredient;

  @Column(nullable = false)
  private String batchNumber;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal quantity;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal costPerUnit;

  @ManyToOne
  @JoinColumn(name = "supplier_id")
  private Supplier supplier;

  @Column private LocalDate receivedDate;

  @Column private LocalDate expiryDate;

  @Column private String qualityStatus; // APPROVED, PENDING, REJECTED

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
