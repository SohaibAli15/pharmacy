/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String invoiceNumber;

  @ManyToOne
  @JoinColumn(name = "store_id", nullable = false)
  private Store store;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @ManyToOne
  @JoinColumn(name = "pharmacist_id", nullable = false)
  private User pharmacist;

  @Column(nullable = false)
  private LocalDateTime saleDate;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal subtotal;

  @Column(precision = 12, scale = 2)
  private BigDecimal discount;

  @Column(precision = 12, scale = 2)
  private BigDecimal taxAmount;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal totalAmount;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private PaymentMethod paymentMethod;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private SaleStatus status;

  @Column private String notes;

  @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<SaleItem> items;

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

  public enum PaymentMethod {
    CASH,
    CREDIT_CARD,
    DEBIT_CARD,
    INSURANCE,
    ONLINE,
    UPI,
    CHECK
  }

  public enum SaleStatus {
    CONFIRMED,
    IN_PRODUCTION,
    DISPATCHED,
    COMPLETED,
    PENDING,
    CANCELLED,
    RETURNED
  }
}
