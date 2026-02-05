/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.AccountDto;
import com.pharmacy.dto.AccountStatementDto;
import com.pharmacy.entity.Account;
import com.pharmacy.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Account Management", description = "APIs for managing party accounts and ledgers")
public class AccountController {

  private final AccountService accountService;

  @PostMapping
  @Operation(
      summary = "Create a new account",
      description = "Create a new ledger account for a party (customer/supplier)")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Account created successfully",
            content = @Content(schema = @Schema(implementation = AccountDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
      })
  public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
    AccountDto created = accountService.createAccount(accountDto);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get account by ID", description = "Retrieve a specific account by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Account found",
            content = @Content(schema = @Schema(implementation = AccountDto.class))),
        @ApiResponse(responseCode = "404", description = "Account not found")
      })
  public ResponseEntity<AccountDto> getAccountById(
      @Parameter(description = "Account ID", required = true) @PathVariable Long id) {
    AccountDto account = accountService.getAccountById(id);
    return ResponseEntity.ok(account);
  }

  @GetMapping("/code/{code}")
  @Operation(summary = "Get account by code", description = "Retrieve an account by its code")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Account found",
            content = @Content(schema = @Schema(implementation = AccountDto.class))),
        @ApiResponse(responseCode = "404", description = "Account not found")
      })
  public ResponseEntity<AccountDto> getAccountByCode(
      @Parameter(description = "Account code", required = true) @PathVariable String code) {
    AccountDto account = accountService.getAccountByCode(code);
    return ResponseEntity.ok(account);
  }

  @GetMapping("/party/{partyId}")
  @Operation(summary = "Get account by party ID", description = "Retrieve account for a party")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Account found",
            content = @Content(schema = @Schema(implementation = AccountDto.class))),
        @ApiResponse(responseCode = "404", description = "Account not found")
      })
  public ResponseEntity<AccountDto> getAccountByPartyId(
      @Parameter(description = "Party ID", required = true) @PathVariable Long partyId) {
    AccountDto account = accountService.getAccountByPartyId(partyId);
    return ResponseEntity.ok(account);
  }

  @GetMapping
  @Operation(summary = "Get all accounts", description = "Retrieve all ledger accounts")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved accounts",
      content = @Content(schema = @Schema(implementation = AccountDto.class)))
  public ResponseEntity<List<AccountDto>> getAllAccounts() {
    List<AccountDto> accounts = accountService.getAllAccounts();
    return ResponseEntity.ok(accounts);
  }

  @GetMapping("/type/{type}")
  @Operation(
      summary = "Get accounts by type",
      description = "Retrieve accounts filtered by type (RECEIVABLE, PAYABLE, BOTH)")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved accounts",
      content = @Content(schema = @Schema(implementation = AccountDto.class)))
  public ResponseEntity<List<AccountDto>> getAccountsByType(
      @Parameter(description = "Account type", required = true) @PathVariable
          Account.AccountType type) {
    List<AccountDto> accounts = accountService.getAccountsByType(type);
    return ResponseEntity.ok(accounts);
  }

  @GetMapping("/{id}/balance")
  @Operation(
      summary = "Get account balance",
      description = "Retrieve current balance of an account")
  @ApiResponse(
      responseCode = "200",
      description = "Balance retrieved",
      content = @Content(schema = @Schema(implementation = java.math.BigDecimal.class)))
  public ResponseEntity<?> getAccountBalance(
      @Parameter(description = "Account ID", required = true) @PathVariable Long id) {
    java.math.BigDecimal balance = accountService.getAccountBalance(id);
    return ResponseEntity.ok(java.util.Map.of("accountId", id, "balance", balance));
  }

  @GetMapping("/{id}/statement")
  @Operation(
      summary = "Get account statement",
      description = "Generate account statement for a date range")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Statement generated",
            content = @Content(schema = @Schema(implementation = AccountStatementDto.class))),
        @ApiResponse(responseCode = "404", description = "Account not found")
      })
  public ResponseEntity<AccountStatementDto> getAccountStatement(
      @Parameter(description = "Account ID", required = true) @PathVariable Long id,
      @Parameter(description = "From date (YYYY-MM-DD)", required = true)
          @RequestParam
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate fromDate,
      @Parameter(description = "To date (YYYY-MM-DD)", required = true)
          @RequestParam
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate toDate) {
    AccountStatementDto statement = accountService.getAccountStatement(id, fromDate, toDate);
    return ResponseEntity.ok(statement);
  }

  @GetMapping("/{id}/transactions")
  @Operation(
      summary = "Get account transactions",
      description = "Retrieve all transactions for an account")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved transactions",
      content = @Content(schema = @Schema(implementation = Object.class)))
  public ResponseEntity<List<?>> getAccountTransactions(
      @Parameter(description = "Account ID", required = true) @PathVariable Long id) {
    List<?> transactions = accountService.getAccountTransactions(id);
    return ResponseEntity.ok(transactions);
  }

  @PatchMapping("/{id}/status")
  @Operation(summary = "Update account status", description = "Update account status")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Status updated",
            content = @Content(schema = @Schema(implementation = AccountDto.class))),
        @ApiResponse(responseCode = "404", description = "Account not found")
      })
  public ResponseEntity<AccountDto> updateAccountStatus(
      @Parameter(description = "Account ID", required = true) @PathVariable Long id,
      @Parameter(description = "New status", required = true) @RequestParam
          Account.AccountStatus status) {
    AccountDto updated = accountService.updateAccountStatus(id, status);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete account", description = "Delete an account by ID")
  @ApiResponse(responseCode = "204", description = "Account deleted")
  public ResponseEntity<Void> deleteAccount(
      @Parameter(description = "Account ID", required = true) @PathVariable Long id) {
    accountService.deleteAccount(id);
    return ResponseEntity.noContent().build();
  }
}
