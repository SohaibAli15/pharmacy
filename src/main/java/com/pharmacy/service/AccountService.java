/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.pharmacy.dto.AccountDto;
import com.pharmacy.dto.AccountStatementDto;

public interface AccountService {
  /** Create account for a party (customer/supplier) */
  AccountDto createAccount(AccountDto accountDto);

  /** Get account by ID */
  AccountDto getAccountById(Long accountId);

  /** Get account by account code */
  AccountDto getAccountByCode(String accountCode);

  /** Get account by party ID */
  AccountDto getAccountByPartyId(Long partyId);

  /** Get all accounts */
  List<AccountDto> getAllAccounts();

  /** Get accounts by type (RECEIVABLE, PAYABLE, BOTH) */
  List<AccountDto> getAccountsByType(com.pharmacy.entity.Account.AccountType accountType);

  /** Get current balance of an account */
  BigDecimal getAccountBalance(Long accountId);

  /** Update account balance (when GL entry is posted) */
  void updateAccountBalance(Long accountId, BigDecimal debitAmount, BigDecimal creditAmount);

  /** Get account statement for a date range */
  AccountStatementDto getAccountStatement(Long accountId, LocalDate fromDate, LocalDate toDate);

  /** Update account status */
  AccountDto updateAccountStatus(Long accountId, com.pharmacy.entity.Account.AccountStatus status);

  /** Delete account */
  void deleteAccount(Long accountId);

  /** Get all transactions for an account */
  List<?> getAccountTransactions(Long accountId);
}
