/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.SalesInvoiceDto;
import com.pharmacy.service.SalesInvoiceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Sales Invoices", description = "Endpoints for managing Sales Invoices")
@RestController
@RequestMapping("/api/v1/sales-invoices")
@RequiredArgsConstructor
public class SalesInvoiceController {
  private final SalesInvoiceService salesInvoiceService;

  @Operation(summary = "Create a new sales invoice", description = "Creates a new Sales Invoice")
  @ApiResponse(
      responseCode = "200",
      description = "Sales invoice created",
      content = @Content(schema = @Schema(implementation = SalesInvoiceDto.class)))
  @PostMapping
  public ResponseEntity<SalesInvoiceDto> create(@RequestBody SalesInvoiceDto dto) {
    return ResponseEntity.ok(salesInvoiceService.create(dto));
  }

  @Operation(summary = "Update a sales invoice", description = "Updates an existing Sales Invoice")
  @ApiResponse(
      responseCode = "200",
      description = "Sales invoice updated",
      content = @Content(schema = @Schema(implementation = SalesInvoiceDto.class)))
  @PutMapping("/{id}")
  public ResponseEntity<SalesInvoiceDto> update(
      @PathVariable Long id, @RequestBody SalesInvoiceDto dto) {
    return ResponseEntity.ok(salesInvoiceService.update(id, dto));
  }

  @Operation(summary = "Delete a sales invoice", description = "Deletes a Sales Invoice by ID")
  @ApiResponse(responseCode = "204", description = "Sales invoice deleted")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    salesInvoiceService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Get a sales invoice by ID",
      description = "Fetches a Sales Invoice by its ID")
  @ApiResponse(
      responseCode = "200",
      description = "Sales invoice found",
      content = @Content(schema = @Schema(implementation = SalesInvoiceDto.class)))
  @GetMapping("/{id}")
  public ResponseEntity<SalesInvoiceDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(salesInvoiceService.getById(id));
  }

  @Operation(summary = "List all sales invoices", description = "Fetches all Sales Invoices")
  @ApiResponse(
      responseCode = "200",
      description = "List of sales invoices",
      content = @Content(schema = @Schema(implementation = SalesInvoiceDto.class)))
  @GetMapping
  public ResponseEntity<List<SalesInvoiceDto>> getAll() {
    return ResponseEntity.ok(salesInvoiceService.getAll());
  }

  @Operation(
      summary = "Change sales invoice status",
      description = "Change the status of a Sales Invoice")
  @ApiResponse(
      responseCode = "200",
      description = "Status changed",
      content = @Content(schema = @Schema(implementation = SalesInvoiceDto.class)))
  @PatchMapping("/{id}/status")
  public ResponseEntity<SalesInvoiceDto> changeStatus(
      @PathVariable Long id, @RequestParam String status) {
    return ResponseEntity.ok(salesInvoiceService.changeStatus(id, status));
  }
}
