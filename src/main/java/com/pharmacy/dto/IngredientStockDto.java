/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientStockDto {
  private Long id;
  private Long storeId;
  private String storeName;
  private Long ingredientId;
  private String ingredientName;
  private String ingredientUnit;
  private String batchNumber;
  private BigDecimal quantity;
  private BigDecimal costPerUnit;
  private Long supplierId;
  private String supplierName;
  private LocalDate receivedDate;
  private LocalDate expiryDate;
  private String qualityStatus;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
