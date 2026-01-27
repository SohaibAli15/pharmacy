/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MaterialIssueDto {
  private Long id;
  private Long productionBatchId;
  private Long salesOrderId;
  private LocalDateTime issueDate;
  private BigDecimal totalQuantityIssued;
  private String status;
}
