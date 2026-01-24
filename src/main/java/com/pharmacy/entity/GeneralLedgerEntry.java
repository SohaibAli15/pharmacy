/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralLedgerEntry {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String entryId;

  @Column(nullable = false)
  private LocalDate date;

  @Column(nullable = false)
  private String account;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal debit;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal credit;

  @Column(nullable = false)
  private String type; // Income, Expense

  @Column(nullable = false)
  private String company; // For multi-company support
}
