/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.KeyCountDto;
import com.pharmacy.dto.ProductionBatchDto;
import com.pharmacy.dto.ProductionBatchPageResponse;
import com.pharmacy.dto.ProductionBatchStatsResponse;
import com.pharmacy.service.ProductionBatchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/production-batches")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
    name = "Production Batches",
    description = "APIs for managing production batches and material consumption tracking (v1)")
public class ProductionBatchController {

  private final ProductionBatchService productionBatchService;

  @Operation(
      summary = "Get all production batches",
      description = "Retrieve all production batches in the system with pagination")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved production batches",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductionBatchPageResponse.class)))
      })
  @GetMapping
  public ResponseEntity<ProductionBatchPageResponse> getAllProductionBatches(
      @PageableDefault(size = 20) Pageable pageable) {
    Page<ProductionBatchDto> batches = productionBatchService.listAll(pageable);
    ProductionBatchPageResponse response = new ProductionBatchPageResponse();
    response.setContent(batches.getContent());
    response.setTotalElements(batches.getTotalElements());
    response.setTotalPages(batches.getTotalPages());
    response.setNumber(batches.getNumber());
    response.setSize(batches.getSize());
    response.setFirst(batches.isFirst());
    response.setLast(batches.isLast());
    response.setEmpty(batches.isEmpty());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/search")
  @Operation(
      summary = "Search production batches",
      description =
          "Search production batches with multiple filter criteria including location, date range, and status with pagination")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved filtered production batches",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductionBatchPageResponse.class)))
      })
  public ResponseEntity<ProductionBatchPageResponse> searchProductionBatches(
      @Parameter(description = "Filter by business location", example = "Butt Brothers")
          @RequestParam(required = false)
          String businessLocation,
      @Parameter(description = "Filter by status (DRAFT, IN_PROGRESS, COMPLETED, FINALIZED)")
          @RequestParam(required = false)
          String status,
      @Parameter(description = "Filter by finalization status") @RequestParam(required = false)
          Boolean finalized,
      @Parameter(description = "Filter by recipe ID") @RequestParam(required = false) Long recipeId,
      @Parameter(description = "Filter by product ID") @RequestParam(required = false)
          Long productId,
      @Parameter(
              description = "Start date for date range filter (ISO 8601 format)",
              example = "2026-01-01T00:00:00")
          @RequestParam(required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime startDate,
      @Parameter(
              description = "End date for date range filter (ISO 8601 format)",
              example = "2026-12-31T23:59:59")
          @RequestParam(required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime endDate,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<ProductionBatchDto> batches =
        productionBatchService.searchProductionBatches(
            businessLocation, status, finalized, recipeId, productId, startDate, endDate, pageable);
    ProductionBatchPageResponse response = new ProductionBatchPageResponse();
    response.setContent(batches.getContent());
    response.setTotalElements(batches.getTotalElements());
    response.setTotalPages(batches.getTotalPages());
    response.setNumber(batches.getNumber());
    response.setSize(batches.getSize());
    response.setFirst(batches.isFirst());
    response.setLast(batches.isLast());
    response.setEmpty(batches.isEmpty());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/recipe/{recipeId}")
  @Operation(
      summary = "Get production batches by recipe",
      description = "Retrieve all production batches for a specific recipe with pagination")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved production batches for the recipe",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductionBatchPageResponse.class)))
      })
  public ResponseEntity<ProductionBatchPageResponse> getProductionBatchesByRecipe(
      @Parameter(description = "Recipe ID") @PathVariable Long recipeId,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<ProductionBatchDto> batches = productionBatchService.getByRecipe(recipeId, pageable);
    ProductionBatchPageResponse response = new ProductionBatchPageResponse();
    response.setContent(batches.getContent());
    response.setTotalElements(batches.getTotalElements());
    response.setTotalPages(batches.getTotalPages());
    response.setNumber(batches.getNumber());
    response.setSize(batches.getSize());
    response.setFirst(batches.isFirst());
    response.setLast(batches.isLast());
    response.setEmpty(batches.isEmpty());
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Get production batches by status",
      description = "Retrieve all production batches with a specific status with pagination")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved production batches by status",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductionBatchPageResponse.class)))
      })
  @GetMapping("/status/{status}")
  public ResponseEntity<ProductionBatchPageResponse> getProductionBatchesByStatus(
      @Parameter(description = "Status value", example = "COMPLETED") @PathVariable String status,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<ProductionBatchDto> batches = productionBatchService.getByStatus(status, pageable);
    ProductionBatchPageResponse response = new ProductionBatchPageResponse();
    response.setContent(batches.getContent());
    response.setTotalElements(batches.getTotalElements());
    response.setTotalPages(batches.getTotalPages());
    response.setNumber(batches.getNumber());
    response.setSize(batches.getSize());
    response.setFirst(batches.isFirst());
    response.setLast(batches.isLast());
    response.setEmpty(batches.isEmpty());
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Get all business locations",
      description = "Retrieve list of all business locations that have production batches")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved locations",
            content =
                @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = String.class))))
      })
  @GetMapping("/locations")
  public ResponseEntity<List<String>> getBusinessLocations() {
    List<String> locations = productionBatchService.getAllBusinessLocations();
    return ResponseEntity.ok(locations);
  }

  @Operation(
      summary = "Get production batch by ID",
      description = "Retrieve a specific production batch with all material consumption details")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Production batch found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductionBatchDto.class))),
        @ApiResponse(responseCode = "404", description = "Production batch not found")
      })
  @GetMapping("/{id}")
  public ResponseEntity<ProductionBatchDto> getProductionBatchById(
      @Parameter(description = "Production batch ID", example = "1") @PathVariable Long id) {
    try {
      ProductionBatchDto dto = productionBatchService.getById(id);
      return ResponseEntity.ok(dto);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(
      summary = "Create new production batch",
      description = "Create a new production batch with material consumption tracking")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Production batch created successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductionBatchDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
      })
  @PostMapping
  public ResponseEntity<?> createProductionBatch(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Production batch details with materials consumed",
              required = true)
          @Valid
          @RequestBody
          ProductionBatchDto dto) {
    try {
      ProductionBatchDto created = productionBatchService.create(dto);
      return ResponseEntity.status(HttpStatus.CREATED).body(created);
    } catch (RuntimeException e) {
      Map<String, String> error = new HashMap<>();
      error.put("error", e.getMessage());
      return ResponseEntity.badRequest().body(error);
    }
  }

  @Operation(
      summary = "Update production batch",
      description =
          "Update production batch details and material consumption (only if not finalized)")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Production batch updated successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductionBatchDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or batch is finalized"),
        @ApiResponse(responseCode = "404", description = "Production batch not found")
      })
  @PutMapping("/{id}")
  public ResponseEntity<?> updateProductionBatch(
      @Parameter(description = "Production batch ID") @PathVariable Long id,
      @Valid @RequestBody ProductionBatchDto dto) {
    try {
      ProductionBatchDto updated = productionBatchService.update(id, dto);
      return ResponseEntity.ok(updated);
    } catch (RuntimeException e) {
      Map<String, String> error = new HashMap<>();
      error.put("error", e.getMessage());
      return ResponseEntity.badRequest().body(error);
    }
  }

  @Operation(
      summary = "Delete production batch",
      description = "Delete a production batch (only if not finalized)")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Production batch deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Cannot delete finalized batch"),
        @ApiResponse(responseCode = "404", description = "Production batch not found")
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteProductionBatch(
      @Parameter(description = "Production batch ID") @PathVariable Long id) {
    try {
      productionBatchService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
      Map<String, String> error = new HashMap<>();
      error.put("error", e.getMessage());
      return ResponseEntity.badRequest().body(error);
    }
  }

  @Operation(
      summary = "Finalize production batch",
      description =
          """
            Finalize production batch - locks the batch from further edits and updates inventory.
            This action:
            - Deducts consumed materials from inventory
            - Adds produced quantity to finished goods inventory
            - Locks the batch from editing
            - Records finalization timestamp and user
            """)
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Production batch finalized successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductionBatchDto.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Batch already finalized or invalid state"),
        @ApiResponse(responseCode = "404", description = "Production batch not found")
      })
  @PostMapping("/{id}/finalize")
  public ResponseEntity<?> finalize(
      @Parameter(description = "Production batch ID") @PathVariable Long id,
      @Parameter(description = "Username of person finalizing", example = "admin")
          @RequestParam(required = false)
          String finalizedBy) {
    try {
      ProductionBatchDto finalized = productionBatchService.finalize(id, finalizedBy);
      return ResponseEntity.ok(finalized);
    } catch (RuntimeException e) {
      Map<String, String> error = new HashMap<>();
      error.put("error", e.getMessage());
      return ResponseEntity.badRequest().body(error);
    }
  }

  @Operation(
      summary = "Unfinalize production batch",
      description =
          """
            Reverse the finalization of a production batch.
            This action:
            - Restores consumed materials to inventory
            - Removes produced quantity from finished goods
            - Unlocks the batch for editing
            - Clears finalization details
            """)
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Production batch unfinalized successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductionBatchDto.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Batch is not finalized or cannot be unfinalized"),
        @ApiResponse(responseCode = "404", description = "Production batch not found")
      })
  @PostMapping("/{id}/unfinalize")
  public ResponseEntity<?> unfinalize(
      @Parameter(description = "Production batch ID") @PathVariable Long id) {
    try {
      ProductionBatchDto unfinalized = productionBatchService.unfinalize(id);
      return ResponseEntity.ok(unfinalized);
    } catch (RuntimeException e) {
      Map<String, String> error = new HashMap<>();
      error.put("error", e.getMessage());
      return ResponseEntity.badRequest().body(error);
    }
  }

  /** Get production statistics */
  @Operation(
      summary = "Get production statistics",
      description = "Retrieve production statistics for all batches or within a date range")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved production statistics",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductionBatchStatsResponse.class)))
      })
  @GetMapping("/stats")
  public ResponseEntity<ProductionBatchStatsResponse> getProductionStats(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime endDate) {
    List<ProductionBatchDto> batches;

    if (startDate != null && endDate != null) {
      batches =
          productionBatchService.searchProductionBatches(
              null, null, null, null, null, startDate, endDate);
    } else {
      batches = productionBatchService.listAll();
    }

    ProductionBatchStatsResponse stats = new ProductionBatchStatsResponse();
    stats.setTotalBatches(batches.size());
    long finalizedCount =
        batches.stream().filter(b -> Boolean.TRUE.equals(b.getIsFinalized())).count();
    long pendingCount =
        batches.stream().filter(b -> !Boolean.TRUE.equals(b.getIsFinalized())).count();
    stats.setFinalizedBatches(finalizedCount);
    stats.setPendingBatches(pendingCount);

    // Group by status
    Map<String, Long> statusCount = new HashMap<>();
    batches.forEach(
        batch -> {
          String status = batch.getStatus() != null ? batch.getStatus() : "UNKNOWN";
          statusCount.put(status, statusCount.getOrDefault(status, 0L) + 1);
        });
    // Convert to list of KeyCountDto for clearer OpenAPI schema
    List<KeyCountDto> statusList =
        statusCount.entrySet().stream()
            .map(e -> new KeyCountDto(e.getKey(), e.getValue()))
            .toList();
    stats.setStatusBreakdown(statusList);

    // Group by location
    Map<String, Long> locationCount = new HashMap<>();
    batches.forEach(
        batch -> {
          String location =
              batch.getBusinessLocation() != null ? batch.getBusinessLocation() : "Unknown";
          locationCount.put(location, locationCount.getOrDefault(location, 0L) + 1);
        });
    List<KeyCountDto> locationList =
        locationCount.entrySet().stream()
            .map(e -> new KeyCountDto(e.getKey(), e.getValue()))
            .toList();
    stats.setLocationBreakdown(locationList);

    return ResponseEntity.ok(stats);
  }
}
