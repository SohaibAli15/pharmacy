/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.entity;

import java.math.BigDecimal;
import java.time.YearMonth;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payroll {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne private Employee employee;

  @Column(nullable = false)
  private YearMonth payrollMonth;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column(nullable = false)
  private String status; // Paid, Pending, etc.
}
