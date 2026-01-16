/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;

import com.pharmacy.entity.PurchaseOrderItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItemDto {
  private Long id;
  private Long purchaseOrderId;
  private Long ingredientId;
  private String ingredientName;
  private String ingredientUnit;
  private BigDecimal quantity;
  private BigDecimal unitPrice;
  private BigDecimal totalPrice;
  private BigDecimal receivedQuantity;
  private String notes;
  private PurchaseOrderItem.ItemStatus status;
}
