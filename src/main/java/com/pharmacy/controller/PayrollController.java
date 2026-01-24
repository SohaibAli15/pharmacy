/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.PayrollDto;
import com.pharmacy.service.PayrollManagementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Payroll Management", description = "Endpoints for managing payroll")
@RestController
@RequestMapping("/api/v1/payroll")
@RequiredArgsConstructor
public class PayrollController {
  private final PayrollManagementService payrollService;

  @Operation(summary = "Create payroll record", description = "Creates a new payroll record")
  @ApiResponse(
      responseCode = "200",
      description = "Payroll created",
      content = @Content(schema = @Schema(implementation = PayrollDto.class)))
  @PostMapping
  public ResponseEntity<PayrollDto> create(@RequestBody PayrollDto dto) {
    return ResponseEntity.ok(payrollService.create(dto));
  }

  @Operation(summary = "Update payroll record", description = "Updates an existing payroll record")
  @ApiResponse(
      responseCode = "200",
      description = "Payroll updated",
      content = @Content(schema = @Schema(implementation = PayrollDto.class)))
  @PutMapping("/{id}")
  public ResponseEntity<PayrollDto> update(@PathVariable Long id, @RequestBody PayrollDto dto) {
    return ResponseEntity.ok(payrollService.update(id, dto));
  }

  @Operation(summary = "Delete payroll record", description = "Deletes a payroll record by ID")
  @ApiResponse(responseCode = "204", description = "Payroll deleted")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    payrollService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Get payroll by ID", description = "Fetches a payroll record by its ID")
  @ApiResponse(
      responseCode = "200",
      description = "Payroll found",
      content = @Content(schema = @Schema(implementation = PayrollDto.class)))
  @GetMapping("/{id}")
  public ResponseEntity<PayrollDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(payrollService.getById(id));
  }

  @Operation(summary = "List all payroll records", description = "Fetches all payroll records")
  @ApiResponse(
      responseCode = "200",
      description = "List of payroll records",
      content =
          @Content(
              mediaType = "application/json",
              array =
                  @io.swagger.v3.oas.annotations.media.ArraySchema(
                      schema = @Schema(implementation = PayrollDto.class))))
  @GetMapping
  public ResponseEntity<List<PayrollDto>> getAll() {
    return ResponseEntity.ok(payrollService.getAll());
  }
}
