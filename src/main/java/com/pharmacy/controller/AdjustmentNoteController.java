/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.AdjustmentNoteDto;
import com.pharmacy.service.AdjustmentNoteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Adjustment Note Management", description = "Endpoints for managing adjustment notes")
@RestController
@RequestMapping("/api/v1/adjustment-notes")
@RequiredArgsConstructor
public class AdjustmentNoteController {
  private final AdjustmentNoteService adjustmentNoteService;

  @Operation(
      summary = "Create a new adjustment note",
      description = "Creates a new adjustment note")
  @ApiResponse(
      responseCode = "200",
      description = "Adjustment note created",
      content = @Content(schema = @Schema(implementation = AdjustmentNoteDto.class)))
  @PostMapping
  public ResponseEntity<AdjustmentNoteDto> create(@RequestBody AdjustmentNoteDto dto) {
    return ResponseEntity.ok(adjustmentNoteService.create(dto));
  }

  @Operation(
      summary = "Update an adjustment note",
      description = "Updates an existing adjustment note")
  @ApiResponse(
      responseCode = "200",
      description = "Adjustment note updated",
      content = @Content(schema = @Schema(implementation = AdjustmentNoteDto.class)))
  @PutMapping("/{id}")
  public ResponseEntity<AdjustmentNoteDto> update(
      @PathVariable Long id, @RequestBody AdjustmentNoteDto dto) {
    return ResponseEntity.ok(adjustmentNoteService.update(id, dto));
  }

  @Operation(
      summary = "Delete an adjustment note",
      description = "Deletes an adjustment note by ID")
  @ApiResponse(responseCode = "204", description = "Adjustment note deleted")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    adjustmentNoteService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Get an adjustment note by ID",
      description = "Fetches an adjustment note by its ID")
  @ApiResponse(
      responseCode = "200",
      description = "Adjustment note found",
      content = @Content(schema = @Schema(implementation = AdjustmentNoteDto.class)))
  @GetMapping("/{id}")
  public ResponseEntity<AdjustmentNoteDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(adjustmentNoteService.getById(id));
  }

  @Operation(summary = "List all adjustment notes", description = "Fetches all adjustment notes")
  @ApiResponse(
      responseCode = "200",
      description = "List of adjustment notes",
      content = @Content(schema = @Schema(implementation = AdjustmentNoteDto.class)))
  @GetMapping
  public ResponseEntity<List<AdjustmentNoteDto>> getAll() {
    return ResponseEntity.ok(adjustmentNoteService.getAll());
  }

  @Operation(
      summary = "List adjustment notes by company",
      description = "Fetches all adjustment notes for a company")
  @ApiResponse(
      responseCode = "200",
      description = "List of adjustment notes",
      content = @Content(schema = @Schema(implementation = AdjustmentNoteDto.class)))
  @GetMapping("/company/{company}")
  public ResponseEntity<List<AdjustmentNoteDto>> getByCompany(@PathVariable String company) {
    return ResponseEntity.ok(adjustmentNoteService.getByCompany(company));
  }
}
