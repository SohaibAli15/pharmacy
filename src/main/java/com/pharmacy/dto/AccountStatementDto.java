/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Account Statement DTO - combines account info with transaction history */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountStatementDto {
  private Long accountId;
  private String accountCode;
  private String accountName;
  private String partyName;
  private String partyCode;
  private LocalDate statementDate;
  private LocalDate fromDate;
  private LocalDate toDate;
  private BigDecimal openingBalance;
  private BigDecimal closingBalance;
  private BigDecimal totalDebits;
  private BigDecimal totalCredits;
  private List<AccountTransactionDto> transactions;
}
