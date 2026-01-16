/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock_transfer_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockTransferItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "stock_transfer_id", nullable = false)
  private StockTransfer stockTransfer;

  @ManyToOne
  @JoinColumn(name = "medicine_id")
  private Medicine medicine;

  @ManyToOne
  @JoinColumn(name = "ingredient_id")
  private Ingredient ingredient;

  @Column private String batchNumber;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal requestedQuantity;

  @Column(precision = 10, scale = 2)
  private BigDecimal approvedQuantity;

  @Column(precision = 10, scale = 2)
  private BigDecimal receivedQuantity;

  @Column private String notes;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ItemStatus status;

  public enum ItemStatus {
    PENDING,
    APPROVED,
    IN_TRANSIT,
    PARTIALLY_RECEIVED,
    RECEIVED,
    CANCELLED
  }
}
