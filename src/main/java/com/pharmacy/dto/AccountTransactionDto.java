/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Represents a single transaction in account statement */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountTransactionDto {
  private Long transactionId;
  private LocalDate date;
  private String description;
  private String transactionType; // SALE, PURCHASE, PAYMENT, RETURN, ADJUSTMENT
  private String referenceNumber; // Invoice/PO number
  private BigDecimal debit;
  private BigDecimal credit;
  private BigDecimal runningBalance;
}
