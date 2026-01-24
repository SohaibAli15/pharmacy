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
public class SalesOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String soNumber;

  private String customer;
  private String product;
  private BigDecimal quantity;
  private BigDecimal dispatched;
  private BigDecimal amount;
  private String workOrder;

  @Enumerated(EnumType.STRING)
  private Status status;

  private LocalDate orderDate;

  public enum Status {
    CONFIRMED,
    IN_PRODUCTION,
    READY_TO_DISPATCH,
    DISPATCHED,
    CANCELLED
  }
}
