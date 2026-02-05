/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Account entity represents the ledger account for a party (customer/supplier) Tracks all debit and
 * credit transactions with running balance
 */
@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String accountCode;

  @Column(nullable = false)
  private String accountName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AccountType accountType; // RECEIVABLE, PAYABLE, BOTH

  @Column(nullable = false)
  private LocalDate accountOpeningDate;

  @Builder.Default
  @Column(nullable = false, precision = 15, scale = 2)
  private BigDecimal openingBalance = BigDecimal.ZERO;

  @Builder.Default
  @Column(nullable = false, precision = 15, scale = 2)
  private BigDecimal currentBalance = BigDecimal.ZERO;

  @Builder.Default
  @Column(nullable = false, precision = 15, scale = 2)
  private BigDecimal totalDebits = BigDecimal.ZERO;

  @Builder.Default
  @Column(nullable = false, precision = 15, scale = 2)
  private BigDecimal totalCredits = BigDecimal.ZERO;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AccountStatus status = AccountStatus.ACTIVE;

  @Column private String notes;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
    if (currentBalance == null) {
      currentBalance = openingBalance;
    }
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  public enum AccountType {
    RECEIVABLE, // Customer owes us money
    PAYABLE, // We owe supplier money
    BOTH // Mixed transactions
  }

  public enum AccountStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED,
    CLOSED
  }
}
