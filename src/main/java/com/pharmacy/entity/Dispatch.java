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
public class Dispatch {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sales_order_id", nullable = false)
  private SalesOrder salesOrder;

  @Column(nullable = false)
  private LocalDateTime dispatchDate;

  @Column(nullable = false, precision = 15, scale = 3)
  private BigDecimal quantityDispatched;

  @Enumerated(EnumType.STRING)
  private Status status;

  public enum Status {
    PENDING,
    DISPATCHED,
    PARTIALLY_DISPATCHED,
    CANCELLED
  }
}
