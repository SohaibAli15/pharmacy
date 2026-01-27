/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock_transfers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockTransfer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String transferNumber;

  @ManyToOne
  @JoinColumn(name = "from_store_id", nullable = false)
  private Store fromStore;

  @ManyToOne
  @JoinColumn(name = "to_store_id", nullable = false)
  private Store toStore;

  @ManyToOne
  @JoinColumn(name = "requested_by", nullable = false)
  private User requestedBy;

  @ManyToOne
  @JoinColumn(name = "approved_by")
  private User approvedBy;

  @ManyToOne
  @JoinColumn(name = "received_by")
  private User receivedBy;

  @Column(nullable = false)
  private LocalDate transferDate;

  @Column private LocalDate expectedArrivalDate;

  @Column private LocalDate actualArrivalDate;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TransferStatus status;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TransferType type;

  @Column private String notes;

  @Column private String shippingMethod;

  @Column private String trackingNumber;

  @OneToMany(mappedBy = "stockTransfer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<StockTransferItem> items;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  public enum TransferStatus {
    DRAFT,
    PENDING_APPROVAL,
    APPROVED,
    IN_TRANSIT,
    PARTIALLY_RECEIVED,
    RECEIVED,
    CANCELLED,
    REJECTED
  }

  public enum TransferType {
    INGREDIENT,
    PRODUCT,
    BOTH
  }
}
