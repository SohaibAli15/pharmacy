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
public class Employee {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String employeeId;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String department;

  @Column(nullable = false)
  private String designation;

  @Column(nullable = false)
  private BigDecimal salary;

  @Column(nullable = false)
  private LocalDate joiningDate;

  @Column(nullable = false)
  private String status; // Active, Inactive, etc.
}
