/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class MaterialIssueDto {
  private Long id;
  private Long productionBatchId;
  private Long saleId; // replaces salesOrderId
  private LocalDateTime issueDate;
  private BigDecimal totalQuantityIssued;
  private String status;

  // Flattened fields
  private Long issuedById;
  private Long approvedById;
  private String department;
  private String purpose;
  private List<Item> items;

  @Data
  public static class Item {
    private Long ingredientId;
    private String ingredientName;
    private String ingredientCode;
    private java.math.BigDecimal quantityRequired;
    private java.math.BigDecimal quantityIssued;
    private String unit;
    private String batchNumber;
    private String expiryDate;
    private String lotNumber;
    private String storageLocation;
    private String notes;
  }
}
