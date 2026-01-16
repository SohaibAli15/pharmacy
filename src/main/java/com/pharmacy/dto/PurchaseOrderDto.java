/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.pharmacy.entity.PurchaseOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderDto {
  private Long id;
  private String orderNumber;
  private Long supplierId;
  private String supplierName;
  private Long storeId;
  private String storeName;
  private Long createdById;
  private String createdByName;
  private LocalDate orderDate;
  private LocalDate expectedDeliveryDate;
  private LocalDate actualDeliveryDate;
  private PurchaseOrder.OrderStatus status;
  private BigDecimal subtotal;
  private BigDecimal taxAmount;
  private BigDecimal shippingCost;
  private BigDecimal totalAmount;
  private String notes;
  private String shippingAddress;
  private List<PurchaseOrderItemDto> items;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
