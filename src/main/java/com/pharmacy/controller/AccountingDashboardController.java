/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pharmacy.service.AdjustmentNoteService;
import com.pharmacy.service.GeneralLedgerEntryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Accounting Dashboard",
    description = "Endpoints for accounting dashboard summary metrics")
@RestController
@RequestMapping("/api/v1/accounting-dashboard")
@RequiredArgsConstructor
public class AccountingDashboardController {
  private final GeneralLedgerEntryService generalLedgerEntryService;
  private final AdjustmentNoteService adjustmentNoteService;

  @Operation(
      summary = "Get accounting dashboard summary",
      description =
          "Returns summary metrics for the accounting dashboard (total revenue, total expenses, net profit, pending adjustments)")
  @ApiResponse(
      responseCode = "200",
      description = "Dashboard summary returned",
      content =
          @Content(
              mediaType = "application/json",
              schema =
                  @Schema(
                      type = "object",
                      example =
                          "{\"totalRevenue\":352500,\"totalExpenses\":235000,\"netProfit\":117500,\"pendingAdjustments\":1}",
                      implementation = java.util.Map.class)))
  @GetMapping("/summary")
  public ResponseEntity<Map<String, Object>> getSummary(@RequestParam String company) {
    // Calculate total revenue (sum of credit for type=Income)
    List<com.pharmacy.dto.GeneralLedgerEntryDto> ledgerEntries =
        generalLedgerEntryService.getByCompany(company);
    BigDecimal totalRevenue =
        ledgerEntries.stream()
            .filter(e -> "Income".equalsIgnoreCase(e.getType()))
            .map(e -> e.getCredit() != null ? e.getCredit() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    // Calculate total expenses (sum of debit for type=Expense)
    BigDecimal totalExpenses =
        ledgerEntries.stream()
            .filter(e -> "Expense".equalsIgnoreCase(e.getType()))
            .map(e -> e.getDebit() != null ? e.getDebit() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    // Net profit = revenue - expenses
    BigDecimal netProfit = totalRevenue.subtract(totalExpenses);
    // Pending adjustments (status = Pending)
    long pendingAdjustments =
        adjustmentNoteService.getByCompany(company).stream()
            .filter(a -> "Pending".equalsIgnoreCase(a.getStatus()))
            .count();
    Map<String, Object> summary = new HashMap<>();
    summary.put("totalRevenue", totalRevenue);
    summary.put("totalExpenses", totalExpenses);
    summary.put("netProfit", netProfit);
    summary.put("pendingAdjustments", pendingAdjustments);
    return ResponseEntity.ok(summary);
  }
}
