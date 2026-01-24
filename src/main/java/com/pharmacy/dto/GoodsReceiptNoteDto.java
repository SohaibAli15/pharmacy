/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class GoodsReceiptNoteDto {
  private Long id;
  private LocalDate date;
  private String status;
  private String referenceNumber;
  private Long purchaseOrderId;
  private String vendor;
  private BigDecimal amount;
}
