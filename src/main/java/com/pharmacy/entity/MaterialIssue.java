/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
  @JoinColumn(name = "sale_id", nullable = true)
  private Sale sales;

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

  @OneToMany(
      mappedBy = "materialIssue",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private List<MaterialIssueItem> items;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "issued_by_user_id")
  private User issuedBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "approved_by_user_id")
  private User approvedBy;

  @Column private String department;

  @Column private String Purpose;

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
