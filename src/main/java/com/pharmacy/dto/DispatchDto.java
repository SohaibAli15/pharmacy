/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DispatchDto {
  private Long id;
  private Long saleId;
  private LocalDateTime dispatchDate;
  private BigDecimal quantityDispatched;
  private String status;
}
