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
public class PurchasePayment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDate date;

  @Enumerated(EnumType.STRING)
  private Status status;

  private String referenceNumber;

  @ManyToOne private PurchaseInvoice invoice;

  private String vendor;

  private BigDecimal amount;

  public enum Status {
    INITIATED,
    COMPLETED,
    CANCELLED
  }
}
