/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderDto {
  private Long id;
  private String workOrderNumber;
  private Long salesOrderId; // Optional, if linking to sales order
  private List<Long> productionBatchIds; // Optional, if linking batches
  private LocalDateTime createdAt;
  private String status;
  private String notes;
}
