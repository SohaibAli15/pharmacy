/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.SupplierDto;
import com.pharmacy.entity.SupplierStatusEntity;
import com.pharmacy.service.SupplierService;

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
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
    name = "Supplier Management",
    description = "APIs for managing suppliers who provide raw ingredients (v1)")
public class SupplierController {

  private final SupplierService supplierService;

  @PostMapping
  @Operation(
      summary = "Create a new supplier",
      description = "Register a new supplier with contact details and payment terms")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Supplier created successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SupplierDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or duplicate supplier code")
      })
  public ResponseEntity<SupplierDto> createSupplier(@RequestBody SupplierDto supplierDto) {
    SupplierDto created = supplierService.createSupplier(supplierDto);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update an existing supplier",
      description = "Update supplier information including contact details and status")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Supplier updated successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SupplierDto.class))),
        @ApiResponse(responseCode = "404", description = "Supplier not found")
      })
  public ResponseEntity<SupplierDto> updateSupplier(
      @Parameter(description = "Supplier ID", required = true) @PathVariable Long id,
      @RequestBody SupplierDto supplierDto) {
    SupplierDto updated = supplierService.updateSupplier(id, supplierDto);
    return ResponseEntity.ok(updated);
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get supplier by ID",
      description = "Retrieve a specific supplier by their ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Supplier found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SupplierDto.class))),
        @ApiResponse(responseCode = "404", description = "Supplier not found")
      })
  public ResponseEntity<SupplierDto> getSupplierById(
      @Parameter(description = "Supplier ID", required = true) @PathVariable Long id) {
    SupplierDto supplier = supplierService.getSupplierById(id);
    return ResponseEntity.ok(supplier);
  }

  @GetMapping("/code/{code}")
  @Operation(
      summary = "Get supplier by code",
      description = "Retrieve a supplier by their unique supplier code")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Supplier found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SupplierDto.class))),
        @ApiResponse(responseCode = "404", description = "Supplier not found")
      })
  public ResponseEntity<SupplierDto> getSupplierByCode(
      @Parameter(description = "Supplier code", required = true) @PathVariable String code) {
    SupplierDto supplier = supplierService.getSupplierByCode(code);
    return ResponseEntity.ok(supplier);
  }

  @GetMapping
  @Operation(summary = "Get all suppliers", description = "Retrieve all registered suppliers")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved all suppliers",
      content =
          @Content(
              mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = SupplierDto.class))))
  public ResponseEntity<List<SupplierDto>> getAllSuppliers() {
    List<SupplierDto> suppliers = supplierService.getAllSuppliers();
    return ResponseEntity.ok(suppliers);
  }

  @GetMapping("/status/{status}")
  @Operation(
      summary = "Get suppliers by status",
      description = "Retrieve suppliers filtered by status (ACTIVE, INACTIVE, SUSPENDED)")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved suppliers",
      content =
          @Content(
              mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = SupplierDto.class))))
  public ResponseEntity<List<SupplierDto>> getSuppliersByStatus(
      @Parameter(description = "Supplier status", required = true) @PathVariable
          SupplierStatusEntity status) {
    List<SupplierDto> suppliers = supplierService.getSuppliersByStatus(status);
    return ResponseEntity.ok(suppliers);
  }

  @GetMapping("/search")
  @Operation(
      summary = "Search suppliers by name",
      description = "Search for suppliers by name or company name")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved matching suppliers",
      content =
          @Content(
              mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = SupplierDto.class))))
  public ResponseEntity<List<SupplierDto>> searchSuppliers(
      @Parameter(description = "Search term (name)", required = true) @RequestParam String name) {
    List<SupplierDto> suppliers = supplierService.searchSuppliersByName(name);
    return ResponseEntity.ok(suppliers);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a supplier", description = "Remove a supplier from the system")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Supplier deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Supplier not found")
      })
  public ResponseEntity<Void> deleteSupplier(
      @Parameter(description = "Supplier ID", required = true) @PathVariable Long id) {
    supplierService.deleteSupplier(id);
    return ResponseEntity.noContent().build();
  }
}
