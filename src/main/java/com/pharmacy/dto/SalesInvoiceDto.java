/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class SalesInvoiceDto {
  private Long id;
  private Long salesOrderId;
  private String invoiceNumber;
  private LocalDate invoiceDate;
  private BigDecimal amount;
  private String status;
}
