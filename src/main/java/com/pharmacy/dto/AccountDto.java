/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.pharmacy.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
  private Long id;
  private String accountCode;
  private String accountName;
  private Account.AccountType accountType; // RECEIVABLE, PAYABLE, BOTH
  private LocalDate accountOpeningDate;
  private BigDecimal openingBalance;
  private BigDecimal currentBalance;
  private BigDecimal totalDebits;
  private BigDecimal totalCredits;
  private Account.AccountStatus status; // ACTIVE, INACTIVE, SUSPENDED, CLOSED
  private String notes;
  private Long partyId;
  private String partyName;
  private String partyCode;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
