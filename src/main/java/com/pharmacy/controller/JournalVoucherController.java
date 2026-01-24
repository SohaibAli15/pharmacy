/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.JournalVoucherDto;
import com.pharmacy.service.JournalVoucherService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Journal Voucher Management", description = "Endpoints for managing journal vouchers")
@RestController
@RequestMapping("/api/v1/journal-vouchers")
@RequiredArgsConstructor
public class JournalVoucherController {
  private final JournalVoucherService journalVoucherService;

  @Operation(
      summary = "Create a new journal voucher",
      description = "Creates a new journal voucher")
  @ApiResponse(
      responseCode = "200",
      description = "Journal voucher created",
      content = @Content(schema = @Schema(implementation = JournalVoucherDto.class)))
  @PostMapping
  public ResponseEntity<JournalVoucherDto> create(@RequestBody JournalVoucherDto dto) {
    return ResponseEntity.ok(journalVoucherService.create(dto));
  }

  @Operation(
      summary = "Update a journal voucher",
      description = "Updates an existing journal voucher")
  @ApiResponse(
      responseCode = "200",
      description = "Journal voucher updated",
      content = @Content(schema = @Schema(implementation = JournalVoucherDto.class)))
  @PutMapping("/{id}")
  public ResponseEntity<JournalVoucherDto> update(
      @PathVariable Long id, @RequestBody JournalVoucherDto dto) {
    return ResponseEntity.ok(journalVoucherService.update(id, dto));
  }

  @Operation(summary = "Delete a journal voucher", description = "Deletes a journal voucher by ID")
  @ApiResponse(responseCode = "204", description = "Journal voucher deleted")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    journalVoucherService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Get a journal voucher by ID",
      description = "Fetches a journal voucher by its ID")
  @ApiResponse(
      responseCode = "200",
      description = "Journal voucher found",
      content = @Content(schema = @Schema(implementation = JournalVoucherDto.class)))
  @GetMapping("/{id}")
  public ResponseEntity<JournalVoucherDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(journalVoucherService.getById(id));
  }

  @Operation(summary = "List all journal vouchers", description = "Fetches all journal vouchers")
  @ApiResponse(
      responseCode = "200",
      description = "List of journal vouchers",
      content = @Content(schema = @Schema(implementation = JournalVoucherDto.class)))
  @GetMapping
  public ResponseEntity<List<JournalVoucherDto>> getAll() {
    return ResponseEntity.ok(journalVoucherService.getAll());
  }

  @Operation(
      summary = "List journal vouchers by company",
      description = "Fetches all journal vouchers for a company")
  @ApiResponse(
      responseCode = "200",
      description = "List of journal vouchers",
      content = @Content(schema = @Schema(implementation = JournalVoucherDto.class)))
  @GetMapping("/company/{company}")
  public ResponseEntity<List<JournalVoucherDto>> getByCompany(@PathVariable String company) {
    return ResponseEntity.ok(journalVoucherService.getByCompany(company));
  }
}
