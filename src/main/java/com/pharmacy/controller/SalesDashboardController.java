/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmacy.service.SalesDashboardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Sales Dashboard", description = "Endpoints for sales management dashboard summary")
@RestController
@RequestMapping("/api/v1/sales-dashboard")
@RequiredArgsConstructor
public class SalesDashboardController {
  private final SalesDashboardService dashboardService;

  @Operation(
      summary = "Get sales dashboard summary",
      description =
          "Returns summary metrics for the sales dashboard (total orders, in production, ready to dispatch, dispatched, revenue collected, pending payments)",
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
                                "{\n  'totalOrders': 2,\n  'inProduction': 1,\n  'readyToDispatch': 0,\n  'dispatched': 0,\n  'revenueCollected': 0,\n  'pendingPayments': 0\n}")))
      })
  @GetMapping("/summary")
  public ResponseEntity<Map<String, Object>> getSummary() {
    return ResponseEntity.ok(dashboardService.getSummary());
  }
}
