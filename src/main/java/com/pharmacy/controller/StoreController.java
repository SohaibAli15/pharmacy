/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.StoreDto;
import com.pharmacy.entity.Store;
import com.pharmacy.service.StoreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
    name = "Store Management",
    description =
        "APIs for managing stores and warehouses (Warehouse, Manufacturing, Retail, Distribution)"
            + " (v1)")
public class StoreController {

  private final StoreService storeService;

  @PostMapping
  @Operation(
      summary = "Create a new store",
      description =
          "Register a new store (Warehouse, Manufacturing Unit, Retail Store, or Distribution"
              + " Center)")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Store created successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StoreDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or duplicate store code")
      })
  public ResponseEntity<StoreDto> createStore(@RequestBody StoreDto storeDto) {
    StoreDto created = storeService.createStore(storeDto);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update an existing store",
      description = "Update store information including location and manager details")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Store updated successfully"),
        @ApiResponse(responseCode = "404", description = "Store not found")
      })
  public ResponseEntity<StoreDto> updateStore(
      @Parameter(description = "Store ID", required = true) @PathVariable Long id,
      @RequestBody StoreDto storeDto) {
    StoreDto updated = storeService.updateStore(id, storeDto);
    return ResponseEntity.ok(updated);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get store by ID", description = "Retrieve a specific store by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Store found"),
        @ApiResponse(responseCode = "404", description = "Store not found")
      })
  public ResponseEntity<StoreDto> getStoreById(
      @Parameter(description = "Store ID", required = true) @PathVariable Long id) {
    StoreDto store = storeService.getStoreById(id);
    return ResponseEntity.ok(store);
  }

  @GetMapping("/code/{code}")
  @Operation(
      summary = "Get store by code",
      description = "Retrieve a store by its unique store code")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Store found"),
        @ApiResponse(responseCode = "404", description = "Store not found")
      })
  public ResponseEntity<StoreDto> getStoreByCode(
      @Parameter(description = "Store code", required = true) @PathVariable String code) {
    StoreDto store = storeService.getStoreByCode(code);
    return ResponseEntity.ok(store);
  }

  @GetMapping
  @Operation(summary = "Get all stores", description = "Retrieve all registered stores")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved all stores")
  public ResponseEntity<List<StoreDto>> getAllStores() {
    List<StoreDto> stores = storeService.getAllStores();
    return ResponseEntity.ok(stores);
  }

  @GetMapping("/status/{status}")
  @Operation(
      summary = "Get stores by status",
      description = "Retrieve stores filtered by status (ACTIVE, INACTIVE, CLOSED)")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved stores")
  public ResponseEntity<List<StoreDto>> getStoresByStatus(
      @Parameter(description = "Store status", required = true) @PathVariable
          Store.StoreStatus status) {
    List<StoreDto> stores = storeService.getStoresByStatus(status);
    return ResponseEntity.ok(stores);
  }

  @GetMapping("/type/{type}")
  @Operation(
      summary = "Get stores by type",
      description =
          "Retrieve stores filtered by type (WAREHOUSE, MANUFACTURING_UNIT, RETAIL_STORE,"
              + " DISTRIBUTION_CENTER)")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved stores")
  public ResponseEntity<List<StoreDto>> getStoresByType(
      @Parameter(description = "Store type", required = true) @PathVariable Store.StoreType type) {
    List<StoreDto> stores = storeService.getStoresByType(type);
    return ResponseEntity.ok(stores);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a store", description = "Remove a store from the system")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Store deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Store not found")
      })
  public ResponseEntity<Void> deleteStore(
      @Parameter(description = "Store ID", required = true) @PathVariable Long id) {
    storeService.deleteStore(id);
    return ResponseEntity.noContent().build();
  }
}
