/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.FoilPrintingDto;
import com.pharmacy.service.FoilPrintingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Foil Printing", description = "Endpoints for managing Foil Printing jobs")
@RestController
@RequestMapping("/api/v1/foil-printing")
@RequiredArgsConstructor
public class FoilPrintingController {
  private final FoilPrintingService foilPrintingService;

  @Operation(
      summary = "Create a new foil printing job",
      description = "Creates a new Foil Printing job")
  @ApiResponse(
      responseCode = "200",
      description = "Foil printing job created",
      content = @Content(schema = @Schema(implementation = FoilPrintingDto.class)))
  @PostMapping
  public ResponseEntity<FoilPrintingDto> create(@RequestBody FoilPrintingDto dto) {
    return ResponseEntity.ok(foilPrintingService.create(dto));
  }

  @Operation(
      summary = "Update a foil printing job",
      description = "Updates an existing Foil Printing job")
  @ApiResponse(
      responseCode = "200",
      description = "Foil printing job updated",
      content = @Content(schema = @Schema(implementation = FoilPrintingDto.class)))
  @PutMapping("/{id}")
  public ResponseEntity<FoilPrintingDto> update(
      @PathVariable Long id, @RequestBody FoilPrintingDto dto) {
    return ResponseEntity.ok(foilPrintingService.update(id, dto));
  }

  @Operation(
      summary = "Delete a foil printing job",
      description = "Deletes a Foil Printing job by ID")
  @ApiResponse(responseCode = "204", description = "Foil printing job deleted")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    foilPrintingService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Get a foil printing job by ID",
      description = "Fetches a Foil Printing job by its ID")
  @ApiResponse(
      responseCode = "200",
      description = "Foil printing job found",
      content = @Content(schema = @Schema(implementation = FoilPrintingDto.class)))
  @GetMapping("/{id}")
  public ResponseEntity<FoilPrintingDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(foilPrintingService.getById(id));
  }

  @Operation(
      summary = "List all foil printing jobs",
      description = "Fetches all Foil Printing jobs")
  @ApiResponse(
      responseCode = "200",
      description = "List of foil printing jobs",
      content = @Content(schema = @Schema(implementation = FoilPrintingDto.class)))
  @GetMapping
  public ResponseEntity<List<FoilPrintingDto>> getAll() {
    return ResponseEntity.ok(foilPrintingService.getAll());
  }
}
