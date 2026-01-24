/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.entity.GoodsReceiptNote;
import com.pharmacy.service.PurchaseDashboardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/purchase-dashboard")
@RequiredArgsConstructor
@Tag(
    name = "Purchase Dashboard Actions",
    description = "Endpoints for purchase management actions such as creating GRN from PO")
public class PurchaseDashboardActionController {
  private final PurchaseDashboardService dashboardService;

  @Operation(
      summary = "Create GRN from Approved Purchase Order",
      description =
          "Creates a Goods Receipt Note (GRN) for an APPROVED Purchase Order. Fails if PO is not APPROVED or GRN already exists.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "GRN created successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GoodsReceiptNote.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request or business rule violation",
            content = @Content)
      })
  @PostMapping("/create-grn/{purchaseOrderId}")
  public ResponseEntity<GoodsReceiptNote> createGRNFromApprovedPO(
      @PathVariable Long purchaseOrderId) {
    return ResponseEntity.ok(dashboardService.createGRNFromApprovedPO(purchaseOrderId));
  }
}
