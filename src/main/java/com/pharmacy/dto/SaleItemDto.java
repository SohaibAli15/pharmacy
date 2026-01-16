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
  private Long medicineId;
  private String medicineName;
  private String medicineCode;
  private Integer quantity;
  private BigDecimal unitPrice;
  private BigDecimal totalPrice;
}
