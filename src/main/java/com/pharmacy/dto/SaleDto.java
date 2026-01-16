/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.pharmacy.entity.Sale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto {
  private Long id;
  private String invoiceNumber;
  private Long storeId;
  private String storeName;
  private Long customerId;
  private String customerName;
  private Long pharmacistId;
  private String pharmacistName;
  private LocalDateTime saleDate;
  private BigDecimal subtotal;
  private BigDecimal discount;
  private BigDecimal taxAmount;
  private BigDecimal totalAmount;
  private Sale.PaymentMethod paymentMethod;
  private Sale.SaleStatus status;
  private String notes;
  private List<SaleItemDto> items;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
