/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleItemDto {
  private Long id;
  private Long saleId;
  private Integer quantity;
  private BigDecimal unitPrice;
  private BigDecimal totalPrice;
  private Long productId;
  private String productName;
  private String productCode;
}
