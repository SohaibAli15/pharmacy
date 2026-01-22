/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.StoreDto;
import com.pharmacy.dto.StorePageResponse;
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
        @ApiResponse(
            responseCode = "200",
            description = "Store updated successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StoreDto.class))),
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
        @ApiResponse(
            responseCode = "200",
            description = "Store found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StoreDto.class))),
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
        @ApiResponse(
            responseCode = "200",
            description = "Store found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StoreDto.class))),
        @ApiResponse(responseCode = "404", description = "Store not found")
      })
  public ResponseEntity<StoreDto> getStoreByCode(
      @Parameter(description = "Store code", required = true) @PathVariable String code) {
    StoreDto store = storeService.getStoreByCode(code);
    return ResponseEntity.ok(store);
  }

  @GetMapping
  @Operation(
      summary = "Get all stores",
      description = "Retrieve all registered stores with pagination")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved stores",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = StorePageResponse.class)))
  public ResponseEntity<StorePageResponse> getAllStores(
      @PageableDefault(size = 20) Pageable pageable) {
    Page<StoreDto> stores = storeService.getAllStores(pageable);
    StorePageResponse response = new StorePageResponse();
    response.setContent(stores.getContent());
    response.setTotalElements(stores.getTotalElements());
    response.setTotalPages(stores.getTotalPages());
    response.setNumber(stores.getNumber());
    response.setSize(stores.getSize());
    response.setFirst(stores.isFirst());
    response.setLast(stores.isLast());
    response.setEmpty(stores.isEmpty());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/status/{status}")
  @Operation(
      summary = "Get stores by status",
      description = "Retrieve stores filtered by status (ACTIVE, INACTIVE, CLOSED) with pagination")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved stores",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = StorePageResponse.class)))
  public ResponseEntity<StorePageResponse> getStoresByStatus(
      @Parameter(description = "Store status", required = true) @PathVariable
          Store.StoreStatus status,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<StoreDto> stores = storeService.getStoresByStatus(status, pageable);
    StorePageResponse response = new StorePageResponse();
    response.setContent(stores.getContent());
    response.setTotalElements(stores.getTotalElements());
    response.setTotalPages(stores.getTotalPages());
    response.setNumber(stores.getNumber());
    response.setSize(stores.getSize());
    response.setFirst(stores.isFirst());
    response.setLast(stores.isLast());
    response.setEmpty(stores.isEmpty());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/type/{type}")
  @Operation(
      summary = "Get stores by type",
      description =
          "Retrieve stores filtered by type (WAREHOUSE, MANUFACTURING_UNIT, RETAIL_STORE,"
              + " DISTRIBUTION_CENTER) with pagination")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved stores",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = StorePageResponse.class)))
  public ResponseEntity<StorePageResponse> getStoresByType(
      @Parameter(description = "Store type", required = true) @PathVariable Store.StoreType type,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<StoreDto> stores = storeService.getStoresByType(type, pageable);
    StorePageResponse response = new StorePageResponse();
    response.setContent(stores.getContent());
    response.setTotalElements(stores.getTotalElements());
    response.setTotalPages(stores.getTotalPages());
    response.setNumber(stores.getNumber());
    response.setSize(stores.getSize());
    response.setFirst(stores.isFirst());
    response.setLast(stores.isLast());
    response.setEmpty(stores.isEmpty());
    return ResponseEntity.ok(response);
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
