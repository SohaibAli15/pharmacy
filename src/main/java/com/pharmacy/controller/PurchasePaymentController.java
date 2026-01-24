/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.PurchasePaymentDto;
import com.pharmacy.service.PurchasePaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Purchase Payments", description = "Endpoints for managing Purchase Payments")
@RestController
@RequestMapping("/api/v1/purchase-payments")
@RequiredArgsConstructor
public class PurchasePaymentController {
  private final PurchasePaymentService paymentService;

  @Operation(summary = "Create a new payment", description = "Creates a new Purchase Payment")
  @ApiResponse(
      responseCode = "200",
      description = "Payment created",
      content = @Content(schema = @Schema(implementation = PurchasePaymentDto.class)))
  @PostMapping
  public ResponseEntity<PurchasePaymentDto> create(@RequestBody PurchasePaymentDto dto) {
    return ResponseEntity.ok(paymentService.create(dto));
  }

  @Operation(summary = "Update a payment", description = "Updates an existing Purchase Payment")
  @ApiResponse(
      responseCode = "200",
      description = "Payment updated",
      content = @Content(schema = @Schema(implementation = PurchasePaymentDto.class)))
  @PutMapping("/{id}")
  public ResponseEntity<PurchasePaymentDto> update(
      @PathVariable Long id, @RequestBody PurchasePaymentDto dto) {
    return ResponseEntity.ok(paymentService.update(id, dto));
  }

  @Operation(summary = "Delete a payment", description = "Deletes a Purchase Payment by ID")
  @ApiResponse(responseCode = "204", description = "Payment deleted")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    paymentService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Get a payment by ID", description = "Fetches a Purchase Payment by its ID")
  @ApiResponse(
      responseCode = "200",
      description = "Payment found",
      content = @Content(schema = @Schema(implementation = PurchasePaymentDto.class)))
  @GetMapping("/{id}")
  public ResponseEntity<PurchasePaymentDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(paymentService.getById(id));
  }

  @Operation(summary = "List all payments", description = "Fetches all Purchase Payments")
  @ApiResponse(
      responseCode = "200",
      description = "List of payments",
      content = @Content(schema = @Schema(implementation = PurchasePaymentDto.class)))
  @GetMapping
  public ResponseEntity<List<PurchasePaymentDto>> getAll() {
    return ResponseEntity.ok(paymentService.getAll());
  }

  @Operation(
      summary = "Change payment status",
      description = "Change the status of a Purchase Payment")
  @ApiResponse(
      responseCode = "200",
      description = "Status changed",
      content = @Content(schema = @Schema(implementation = PurchasePaymentDto.class)))
  @PatchMapping("/{id}/status")
  public ResponseEntity<PurchasePaymentDto> changeStatus(
      @PathVariable Long id, @RequestParam String status) {
    return ResponseEntity.ok(paymentService.changeStatus(id, status));
  }
}
