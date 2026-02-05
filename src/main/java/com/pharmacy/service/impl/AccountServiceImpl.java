/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.AccountDto;
import com.pharmacy.dto.AccountStatementDto;
import com.pharmacy.dto.AccountTransactionDto;
import com.pharmacy.entity.Account;
import com.pharmacy.entity.GeneralLedgerEntry;
import com.pharmacy.repository.AccountRepository;
import com.pharmacy.repository.GeneralLedgerEntryRepository;
import com.pharmacy.repository.PartyRepository;
import com.pharmacy.service.AccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;
  private final PartyRepository partyRepository;
  private final GeneralLedgerEntryRepository ledgerEntryRepository;

  @Override
  @Transactional
  public AccountDto createAccount(AccountDto accountDto) {
    log.info("Creating account: {}", accountDto.getAccountCode());

    Account account = new Account();
    account.setAccountCode(accountDto.getAccountCode());
    account.setAccountName(accountDto.getAccountName());
    account.setAccountType(accountDto.getAccountType());
    account.setAccountOpeningDate(
        accountDto.getAccountOpeningDate() != null
            ? accountDto.getAccountOpeningDate()
            : LocalDate.now());
    account.setOpeningBalance(
        accountDto.getOpeningBalance() != null ? accountDto.getOpeningBalance() : BigDecimal.ZERO);
    account.setCurrentBalance(
        accountDto.getOpeningBalance() != null ? accountDto.getOpeningBalance() : BigDecimal.ZERO);
    account.setStatus(
        accountDto.getStatus() != null ? accountDto.getStatus() : Account.AccountStatus.ACTIVE);
    account.setNotes(accountDto.getNotes());

    Account savedAccount = accountRepository.save(account);
    log.info("Account created successfully: {}", savedAccount.getId());

    return mapToDto(savedAccount);
  }

  @Override
  @Transactional(readOnly = true)
  public AccountDto getAccountById(Long accountId) {
    log.debug("Fetching account: {}", accountId);
    Account account =
        accountRepository
            .findById(accountId)
            .orElseThrow(() -> new RuntimeException("Account not found: " + accountId));
    return mapToDto(account);
  }

  @Override
  @Transactional(readOnly = true)
  public AccountDto getAccountByCode(String accountCode) {
    log.debug("Fetching account by code: {}", accountCode);
    Account account =
        accountRepository
            .findByAccountCode(accountCode)
            .orElseThrow(() -> new RuntimeException("Account not found: " + accountCode));
    return mapToDto(account);
  }

  @Override
  @Transactional(readOnly = true)
  public AccountDto getAccountByPartyId(Long partyId) {
    log.debug("Fetching account by party: {}", partyId);
    Account account =
        accountRepository
            .findByPartyId(partyId)
            .orElseThrow(() -> new RuntimeException("Account not found for party: " + partyId));
    return mapToDto(account);
  }

  @Override
  @Transactional(readOnly = true)
  public List<AccountDto> getAllAccounts() {
    log.debug("Fetching all accounts");
    return accountRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<AccountDto> getAccountsByType(Account.AccountType accountType) {
    log.debug("Fetching accounts by type: {}", accountType);
    return accountRepository.findByAccountType(accountType).stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public BigDecimal getAccountBalance(Long accountId) {
    log.debug("Fetching balance for account: {}", accountId);
    Account account =
        accountRepository
            .findById(accountId)
            .orElseThrow(() -> new RuntimeException("Account not found: " + accountId));
    return account.getCurrentBalance();
  }

  @Override
  @Transactional
  public void updateAccountBalance(
      Long accountId, BigDecimal debitAmount, BigDecimal creditAmount) {
    log.debug(
        "Updating balance for account: {}, debit: {}, credit: {}",
        accountId,
        debitAmount,
        creditAmount);

    Account account =
        accountRepository
            .findById(accountId)
            .orElseThrow(() -> new RuntimeException("Account not found: " + accountId));

    BigDecimal debit = debitAmount != null ? debitAmount : BigDecimal.ZERO;
    BigDecimal credit = creditAmount != null ? creditAmount : BigDecimal.ZERO;

    // Update totals
    account.setTotalDebits(account.getTotalDebits().add(debit));
    account.setTotalCredits(account.getTotalCredits().add(credit));

    // Calculate new balance based on account type
    // RECEIVABLE: Debit increases balance, Credit decreases
    // PAYABLE: Credit increases balance, Debit decreases
    // BOTH: Follow general accounting rules
    if (account.getAccountType() == Account.AccountType.RECEIVABLE) {
      account.setCurrentBalance(account.getCurrentBalance().add(debit).subtract(credit));
    } else if (account.getAccountType() == Account.AccountType.PAYABLE) {
      account.setCurrentBalance(account.getCurrentBalance().subtract(debit).add(credit));
    } else {
      // BOTH type - use general accounting: assets/receivables increase with debit
      account.setCurrentBalance(account.getCurrentBalance().add(debit).subtract(credit));
    }

    account.setUpdatedAt(LocalDateTime.now());
    accountRepository.save(account);

    log.debug("Account balance updated. New balance: {}", account.getCurrentBalance());
  }

  @Override
  @Transactional(readOnly = true)
  public AccountStatementDto getAccountStatement(
      Long accountId, LocalDate fromDate, LocalDate toDate) {
    log.debug("Generating statement for account: {} from {} to {}", accountId, fromDate, toDate);

    Account account =
        accountRepository
            .findById(accountId)
            .orElseThrow(() -> new RuntimeException("Account not found: " + accountId));

    // Find all GL entries for this account within date range
    List<GeneralLedgerEntry> entries =
        ledgerEntryRepository.findAll().stream()
            .filter(
                e ->
                    (e.getLinkedAccount() != null && e.getLinkedAccount().getId().equals(accountId))
                        || (e.getParty() != null
                            && e.getParty().getAccount() != null
                            && e.getParty().getAccount().getId().equals(accountId)))
            .filter(e -> !e.getDate().isBefore(fromDate) && !e.getDate().isAfter(toDate))
            .collect(Collectors.toList());

    // Build statement
    AccountStatementDto statement = new AccountStatementDto();
    statement.setAccountId(accountId);
    statement.setAccountCode(account.getAccountCode());
    statement.setAccountName(account.getAccountName());
    statement.setStatementDate(LocalDate.now());
    statement.setFromDate(fromDate);
    statement.setToDate(toDate);
    statement.setOpeningBalance(account.getOpeningBalance());
    statement.setClosingBalance(account.getCurrentBalance());

    BigDecimal totalDebits = BigDecimal.ZERO;
    BigDecimal totalCredits = BigDecimal.ZERO;
    BigDecimal runningBalance = account.getOpeningBalance();
    Account.AccountType accType = account.getAccountType();

    List<AccountTransactionDto> transactions = new java.util.ArrayList<>();

    for (GeneralLedgerEntry entry : entries) {
      AccountTransactionDto tx = new AccountTransactionDto();
      tx.setTransactionId(entry.getId());
      tx.setDate(entry.getDate());
      tx.setDescription(entry.getDescription());
      tx.setReferenceNumber(entry.getEntryId());
      tx.setDebit(entry.getDebit());
      tx.setCredit(entry.getCredit());

      // Calculate running balance based on account type
      if (accType == Account.AccountType.RECEIVABLE) {
        runningBalance = runningBalance.add(entry.getDebit()).subtract(entry.getCredit());
      } else if (accType == Account.AccountType.PAYABLE) {
        runningBalance = runningBalance.subtract(entry.getDebit()).add(entry.getCredit());
      }
      tx.setRunningBalance(runningBalance);

      transactions.add(tx);
      totalDebits = totalDebits.add(entry.getDebit() != null ? entry.getDebit() : BigDecimal.ZERO);
      totalCredits =
          totalCredits.add(entry.getCredit() != null ? entry.getCredit() : BigDecimal.ZERO);
    }

    statement.setTotalDebits(totalDebits);
    statement.setTotalCredits(totalCredits);
    statement.setTransactions(transactions);

    log.debug("Statement generated with {} transactions", transactions.size());
    return statement;
  }

  @Override
  @Transactional
  public AccountDto updateAccountStatus(Long accountId, Account.AccountStatus status) {
    log.info("Updating account status: {} to {}", accountId, status);

    Account account =
        accountRepository
            .findById(accountId)
            .orElseThrow(() -> new RuntimeException("Account not found: " + accountId));

    account.setStatus(status);
    account.setUpdatedAt(LocalDateTime.now());

    Account updatedAccount = accountRepository.save(account);
    log.info("Account status updated successfully");

    return mapToDto(updatedAccount);
  }

  @Override
  @Transactional
  public void deleteAccount(Long accountId) {
    log.info("Deleting account: {}", accountId);

    Account account =
        accountRepository
            .findById(accountId)
            .orElseThrow(() -> new RuntimeException("Account not found: " + accountId));

    accountRepository.delete(account);
    log.info("Account deleted successfully");
  }

  @Override
  @Transactional(readOnly = true)
  public List<?> getAccountTransactions(Long accountId) {
    log.debug("Fetching transactions for account: {}", accountId);

    return ledgerEntryRepository.findAll().stream()
        .filter(
            e ->
                (e.getLinkedAccount() != null && e.getLinkedAccount().getId().equals(accountId))
                    || (e.getParty() != null
                        && e.getParty().getAccount() != null
                        && e.getParty().getAccount().getId().equals(accountId)))
        .collect(Collectors.toList());
  }

  private AccountDto mapToDto(Account account) {
    AccountDto dto = new AccountDto();
    dto.setId(account.getId());
    dto.setAccountCode(account.getAccountCode());
    dto.setAccountName(account.getAccountName());
    dto.setAccountType(account.getAccountType());
    dto.setAccountOpeningDate(account.getAccountOpeningDate());
    dto.setOpeningBalance(account.getOpeningBalance());
    dto.setCurrentBalance(account.getCurrentBalance());
    dto.setTotalDebits(account.getTotalDebits());
    dto.setTotalCredits(account.getTotalCredits());
    dto.setStatus(account.getStatus());
    dto.setNotes(account.getNotes());
    dto.setCreatedAt(account.getCreatedAt());
    dto.setUpdatedAt(account.getUpdatedAt());
    return dto;
  }
}
