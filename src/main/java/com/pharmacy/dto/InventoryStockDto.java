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
public class InventoryStockDto {
  private Long id;
  private Long storeId;
  private String storeName;
  private Long productId;
  private String productName;
  private String batchNumber;
  private Integer quantity;
  private BigDecimal costPrice;
  private BigDecimal sellingPrice;
  private LocalDate manufacturingDate;
  private LocalDate expiryDate;
  private Integer reorderLevel;
  private Integer maxStockLevel;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
