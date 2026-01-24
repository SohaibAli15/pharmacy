/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.SalesOrderDto;
import com.pharmacy.service.SalesOrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Sales Orders", description = "Endpoints for managing Sales Orders")
@RestController
@RequestMapping("/api/v1/sales-orders")
@RequiredArgsConstructor
public class SalesOrderController {
  private final SalesOrderService salesOrderService;

  @Operation(summary = "Create a new sales order", description = "Creates a new Sales Order")
  @ApiResponse(
      responseCode = "200",
      description = "Sales order created",
      content = @Content(schema = @Schema(implementation = SalesOrderDto.class)))
  @PostMapping
  public ResponseEntity<SalesOrderDto> create(@RequestBody SalesOrderDto dto) {
    return ResponseEntity.ok(salesOrderService.create(dto));
  }

  @Operation(summary = "Update a sales order", description = "Updates an existing Sales Order")
  @ApiResponse(
      responseCode = "200",
      description = "Sales order updated",
      content = @Content(schema = @Schema(implementation = SalesOrderDto.class)))
  @PutMapping("/{id}")
  public ResponseEntity<SalesOrderDto> update(
      @PathVariable Long id, @RequestBody SalesOrderDto dto) {
    return ResponseEntity.ok(salesOrderService.update(id, dto));
  }

  @Operation(summary = "Delete a sales order", description = "Deletes a Sales Order by ID")
  @ApiResponse(responseCode = "204", description = "Sales order deleted")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    salesOrderService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Get a sales order by ID", description = "Fetches a Sales Order by its ID")
  @ApiResponse(
      responseCode = "200",
      description = "Sales order found",
      content = @Content(schema = @Schema(implementation = SalesOrderDto.class)))
  @GetMapping("/{id}")
  public ResponseEntity<SalesOrderDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(salesOrderService.getById(id));
  }

  @Operation(summary = "List all sales orders", description = "Fetches all Sales Orders")
  @ApiResponse(
      responseCode = "200",
      description = "List of sales orders",
      content = @Content(schema = @Schema(implementation = SalesOrderDto.class)))
  @GetMapping
  public ResponseEntity<List<SalesOrderDto>> getAll() {
    return ResponseEntity.ok(salesOrderService.getAll());
  }

  @Operation(
      summary = "Change sales order status",
      description = "Change the status of a Sales Order")
  @ApiResponse(
      responseCode = "200",
      description = "Status changed",
      content = @Content(schema = @Schema(implementation = SalesOrderDto.class)))
  @PatchMapping("/{id}/status")
  public ResponseEntity<SalesOrderDto> changeStatus(
      @PathVariable Long id, @RequestParam String status) {
    return ResponseEntity.ok(salesOrderService.changeStatus(id, status));
  }
}
