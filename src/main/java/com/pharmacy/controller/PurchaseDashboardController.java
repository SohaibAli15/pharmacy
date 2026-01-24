/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmacy.service.PurchaseDashboardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Purchase Dashboard",
    description = "Endpoints for purchase management dashboard summary")
@RestController
@RequestMapping("/api/v1/purchase-dashboard")
@RequiredArgsConstructor
public class PurchaseDashboardController {
  private final PurchaseDashboardService dashboardService;

  @Operation(
      summary = "Get purchase dashboard summary",
      description =
          "Returns summary metrics for the purchase dashboard (total POs, pending GRN, received, pending payments, total spend)",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Summary metrics",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(
                            example =
                                "{\n  'totalPOs': 2,\n  'pendingGRN': 1,\n  'received': 1,\n  'pendingPayments': 0,\n  'totalSpend': 342225\n}")))
      })
  @GetMapping("/summary")
  public ResponseEntity<Map<String, Object>> getSummary() {
    return ResponseEntity.ok(dashboardService.getSummary());
  }
}
