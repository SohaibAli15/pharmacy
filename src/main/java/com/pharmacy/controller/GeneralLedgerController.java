/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.GeneralLedgerEntryDto;
import com.pharmacy.service.GeneralLedgerEntryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "General Ledger Management",
    description = "Endpoints for managing general ledger entries")
@RestController
@RequestMapping("/api/v1/general-ledger")
@RequiredArgsConstructor
public class GeneralLedgerController {
  private final GeneralLedgerEntryService ledgerService;

  @Operation(
      summary = "Create a new ledger entry",
      description = "Creates a new general ledger entry")
  @ApiResponse(
      responseCode = "200",
      description = "Ledger entry created",
      content = @Content(schema = @Schema(implementation = GeneralLedgerEntryDto.class)))
  @PostMapping
  public ResponseEntity<GeneralLedgerEntryDto> create(@RequestBody GeneralLedgerEntryDto dto) {
    return ResponseEntity.ok(ledgerService.create(dto));
  }

  @Operation(
      summary = "Update a ledger entry",
      description = "Updates an existing general ledger entry")
  @ApiResponse(
      responseCode = "200",
      description = "Ledger entry updated",
      content = @Content(schema = @Schema(implementation = GeneralLedgerEntryDto.class)))
  @PutMapping("/{id}")
  public ResponseEntity<GeneralLedgerEntryDto> update(
      @PathVariable Long id, @RequestBody GeneralLedgerEntryDto dto) {
    return ResponseEntity.ok(ledgerService.update(id, dto));
  }

  @Operation(
      summary = "Delete a ledger entry",
      description = "Deletes a general ledger entry by ID")
  @ApiResponse(responseCode = "204", description = "Ledger entry deleted")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    ledgerService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Get a ledger entry by ID",
      description = "Fetches a general ledger entry by its ID")
  @ApiResponse(
      responseCode = "200",
      description = "Ledger entry found",
      content = @Content(schema = @Schema(implementation = GeneralLedgerEntryDto.class)))
  @GetMapping("/{id}")
  public ResponseEntity<GeneralLedgerEntryDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(ledgerService.getById(id));
  }

  @Operation(
      summary = "List all ledger entries",
      description = "Fetches all general ledger entries")
  @ApiResponse(
      responseCode = "200",
      description = "List of ledger entries",
      content = @Content(schema = @Schema(implementation = GeneralLedgerEntryDto.class)))
  @GetMapping
  public ResponseEntity<List<GeneralLedgerEntryDto>> getAll() {
    return ResponseEntity.ok(ledgerService.getAll());
  }

  @Operation(
      summary = "List ledger entries by company",
      description = "Fetches all general ledger entries for a company")
  @ApiResponse(
      responseCode = "200",
      description = "List of ledger entries",
      content = @Content(schema = @Schema(implementation = GeneralLedgerEntryDto.class)))
  @GetMapping("/company/{company}")
  public ResponseEntity<List<GeneralLedgerEntryDto>> getByCompany(@PathVariable String company) {
    return ResponseEntity.ok(ledgerService.getByCompany(company));
  }
}
