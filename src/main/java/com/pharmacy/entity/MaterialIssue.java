/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialIssue {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "production_batch_id", nullable = false)
  private ProductionBatch productionBatch;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sales_order_id", nullable = false)
  private SalesOrder salesOrder;

  @Column(nullable = false)
  private LocalDateTime issueDate;

  @Column(nullable = false, precision = 15, scale = 3)
  private BigDecimal totalQuantityIssued;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(nullable = false)
  private java.time.LocalDateTime createdAt;

  @Column(nullable = false)
  private java.time.LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = java.time.LocalDateTime.now();
    updatedAt = java.time.LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = java.time.LocalDateTime.now();
  }

  public enum Status {
    PENDING,
    ISSUED,
    PARTIALLY_ISSUED,
    CANCELLED
  }
}
