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
    name = "inventory_stock",
    uniqueConstraints = {
      @UniqueConstraint(columnNames = {"store_id", "product_id", "batch_number"})
    })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryStock {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "store_id", nullable = false)
  private Store store;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Column(nullable = false)
  private String batchNumber;

  @Column(nullable = false)
  private Integer quantity;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal costPrice;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal sellingPrice;

  @Column private LocalDate manufacturingDate;

  @Column(nullable = false)
  private LocalDate expiryDate;

  @Column private Integer reorderLevel;

  @Column private Integer maxStockLevel;

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
