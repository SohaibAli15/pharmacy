/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.GoodsReceiptNoteDto;
import com.pharmacy.service.GoodsReceiptNoteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Goods Receipt Notes", description = "Endpoints for managing Goods Receipt Notes (GRN)")
@RestController
@RequestMapping("/api/v1/grns")
@RequiredArgsConstructor
public class GoodsReceiptNoteController {
  private final GoodsReceiptNoteService grnService;

  @Operation(summary = "Create a new GRN", description = "Creates a new Goods Receipt Note")
  @ApiResponse(
      responseCode = "200",
      description = "GRN created",
      content = @Content(schema = @Schema(implementation = GoodsReceiptNoteDto.class)))
  @PostMapping
  public ResponseEntity<GoodsReceiptNoteDto> create(@RequestBody GoodsReceiptNoteDto dto) {
    return ResponseEntity.ok(grnService.create(dto));
  }

  @Operation(summary = "Update a GRN", description = "Updates an existing Goods Receipt Note")
  @ApiResponse(
      responseCode = "200",
      description = "GRN updated",
      content = @Content(schema = @Schema(implementation = GoodsReceiptNoteDto.class)))
  @PutMapping("/{id}")
  public ResponseEntity<GoodsReceiptNoteDto> update(
      @PathVariable Long id, @RequestBody GoodsReceiptNoteDto dto) {
    return ResponseEntity.ok(grnService.update(id, dto));
  }

  @Operation(summary = "Delete a GRN", description = "Deletes a Goods Receipt Note by ID")
  @ApiResponse(responseCode = "204", description = "GRN deleted")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    grnService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Get a GRN by ID", description = "Fetches a Goods Receipt Note by its ID")
  @ApiResponse(
      responseCode = "200",
      description = "GRN found",
      content = @Content(schema = @Schema(implementation = GoodsReceiptNoteDto.class)))
  @GetMapping("/{id}")
  public ResponseEntity<GoodsReceiptNoteDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(grnService.getById(id));
  }

  @Operation(summary = "List all GRNs", description = "Fetches all Goods Receipt Notes")
  @ApiResponse(
      responseCode = "200",
      description = "List of GRNs",
      content = @Content(schema = @Schema(implementation = GoodsReceiptNoteDto.class)))
  @GetMapping
  public ResponseEntity<List<GoodsReceiptNoteDto>> getAll() {
    return ResponseEntity.ok(grnService.getAll());
  }

  @Operation(
      summary = "Change GRN status",
      description = "Change the status of a Goods Receipt Note")
  @ApiResponse(
      responseCode = "200",
      description = "Status changed",
      content = @Content(schema = @Schema(implementation = GoodsReceiptNoteDto.class)))
  @PatchMapping("/{id}/status")
  public ResponseEntity<GoodsReceiptNoteDto> changeStatus(
      @PathVariable Long id, @RequestParam String status) {
    return ResponseEntity.ok(grnService.changeStatus(id, status));
  }
}
