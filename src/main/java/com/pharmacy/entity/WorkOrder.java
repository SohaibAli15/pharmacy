/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String workOrderNumber;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sales_order_id", nullable = false)
  private SalesOrder salesOrder;

  @OneToMany(mappedBy = "workOrder")
  private List<ProductionBatch> productionBatches;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Enumerated(EnumType.STRING)
  private Status status;

  private String notes;

  public enum Status {
    CREATED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
  }
}
