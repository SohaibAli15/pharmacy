/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.PurchaseInvoiceDto;
import com.pharmacy.service.PurchaseInvoiceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Purchase Invoices", description = "Endpoints for managing Purchase Invoices")
@RestController
@RequestMapping("/api/v1/purchase-invoices")
@RequiredArgsConstructor
public class PurchaseInvoiceController {
  private final PurchaseInvoiceService invoiceService;

  @Operation(summary = "Create a new invoice", description = "Creates a new Purchase Invoice")
  @ApiResponse(
      responseCode = "200",
      description = "Invoice created",
      content = @Content(schema = @Schema(implementation = PurchaseInvoiceDto.class)))
  @PostMapping
  public ResponseEntity<PurchaseInvoiceDto> create(@RequestBody PurchaseInvoiceDto dto) {
    return ResponseEntity.ok(invoiceService.create(dto));
  }

  @Operation(summary = "Update an invoice", description = "Updates an existing Purchase Invoice")
  @ApiResponse(
      responseCode = "200",
      description = "Invoice updated",
      content = @Content(schema = @Schema(implementation = PurchaseInvoiceDto.class)))
  @PutMapping("/{id}")
  public ResponseEntity<PurchaseInvoiceDto> update(
      @PathVariable Long id, @RequestBody PurchaseInvoiceDto dto) {
    return ResponseEntity.ok(invoiceService.update(id, dto));
  }

  @Operation(summary = "Delete an invoice", description = "Deletes a Purchase Invoice by ID")
  @ApiResponse(responseCode = "204", description = "Invoice deleted")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    invoiceService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Get an invoice by ID", description = "Fetches a Purchase Invoice by its ID")
  @ApiResponse(
      responseCode = "200",
      description = "Invoice found",
      content = @Content(schema = @Schema(implementation = PurchaseInvoiceDto.class)))
  @GetMapping("/{id}")
  public ResponseEntity<PurchaseInvoiceDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(invoiceService.getById(id));
  }

  @Operation(summary = "List all invoices", description = "Fetches all Purchase Invoices")
  @ApiResponse(
      responseCode = "200",
      description = "List of invoices",
      content = @Content(schema = @Schema(implementation = PurchaseInvoiceDto.class)))
  @GetMapping
  public ResponseEntity<List<PurchaseInvoiceDto>> getAll() {
    return ResponseEntity.ok(invoiceService.getAll());
  }

  @Operation(
      summary = "Change invoice status",
      description = "Change the status of a Purchase Invoice")
  @ApiResponse(
      responseCode = "200",
      description = "Status changed",
      content = @Content(schema = @Schema(implementation = PurchaseInvoiceDto.class)))
  @PatchMapping("/{id}/status")
  public ResponseEntity<PurchaseInvoiceDto> changeStatus(
      @PathVariable Long id, @RequestParam String status) {
    return ResponseEntity.ok(invoiceService.changeStatus(id, status));
  }
}
