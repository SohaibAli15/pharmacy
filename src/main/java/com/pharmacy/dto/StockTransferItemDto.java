/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;

import com.pharmacy.entity.StockTransferItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockTransferItemDto {
  private Long id;
  private Long stockTransferId;
  private Long productId;
  private String productName;
  private Long ingredientId;
  private String ingredientName;
  private String batchNumber;
  private BigDecimal requestedQuantity;
  private BigDecimal approvedQuantity;
  private BigDecimal receivedQuantity;
  private String notes;
  private StockTransferItem.ItemStatus status;
}
