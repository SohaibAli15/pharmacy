/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialIssueItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "material_issue_id", nullable = false)
  private MaterialIssue materialIssue;

  private Long ingredientId;
  private String ingredientName;
  private String ingredientCode;
  private BigDecimal quantityRequired;
  private BigDecimal quantityIssued;
  private String unit;
  private String batchNumber;
  private String expiryDate;
  private String lotNumber;
  private String storageLocation;
  private String notes;
}
