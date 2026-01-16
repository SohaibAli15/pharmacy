/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.InventoryStockDto;
import com.pharmacy.service.InventoryStockService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/inventory-stock")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
    name = "Inventory Stock Management",
    description =
        "APIs for managing medicine inventory in stores with FIFO tracking and expiry alerts (v1)")
public class InventoryStockController {

  private final InventoryStockService inventoryStockService;

  @PostMapping
  @Operation(
      summary = "Add new inventory stock",
      description =
          "Add new medicine stock to a store with batch tracking, expiry dates, and pricing")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Inventory stock added successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = InventoryStockDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or duplicate batch"),
        @ApiResponse(responseCode = "404", description = "Store or medicine not found")
      })
  public ResponseEntity<InventoryStockDto> addInventoryStock(
      @RequestBody InventoryStockDto inventoryStockDto) {
    InventoryStockDto created = inventoryStockService.addInventoryStock(inventoryStockDto);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update inventory stock",
      description =
          "Update existing inventory stock information including pricing and reorder levels")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Inventory stock updated successfully"),
        @ApiResponse(responseCode = "404", description = "Inventory stock not found")
      })
  public ResponseEntity<InventoryStockDto> updateInventoryStock(
      @Parameter(description = "Inventory stock ID", required = true) @PathVariable Long id,
      @RequestBody InventoryStockDto inventoryStockDto) {
    InventoryStockDto updated = inventoryStockService.updateInventoryStock(id, inventoryStockDto);
    return ResponseEntity.ok(updated);
  }

  @PostMapping("/{id}/adjust")
  @Operation(
      summary = "Adjust stock quantity",
      description =
          "Adjust inventory quantity (positive for addition, negative for reduction) with reason tracking")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Stock adjusted successfully"),
        @ApiResponse(responseCode = "400", description = "Insufficient stock for reduction"),
        @ApiResponse(responseCode = "404", description = "Inventory stock not found")
      })
  public ResponseEntity<Void> adjustStock(
      @Parameter(description = "Inventory stock ID", required = true) @PathVariable Long id,
      @Parameter(
              description = "Quantity change (positive to add, negative to reduce)",
              required = true)
          @RequestParam
          int quantityChange,
      @Parameter(description = "Reason for adjustment", required = true) @RequestParam
          String reason) {
    inventoryStockService.adjustStock(id, quantityChange, reason);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get inventory stock by ID",
      description = "Retrieve a specific inventory stock entry by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Inventory stock found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = InventoryStockDto.class))),
        @ApiResponse(responseCode = "404", description = "Inventory stock not found")
      })
  public ResponseEntity<InventoryStockDto> getInventoryStockById(
      @Parameter(description = "Inventory stock ID", required = true) @PathVariable Long id) {
    InventoryStockDto stock = inventoryStockService.getInventoryStockById(id);
    return ResponseEntity.ok(stock);
  }

  @GetMapping
  @Operation(
      summary = "Get all inventory stock",
      description = "Retrieve all inventory stock entries across all stores")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved all inventory stock")
  public ResponseEntity<List<InventoryStockDto>> getAllInventoryStock() {
    List<InventoryStockDto> stocks = inventoryStockService.getAllInventoryStock();
    return ResponseEntity.ok(stocks);
  }

  @GetMapping("/store/{storeId}")
  @Operation(
      summary = "Get inventory by store",
      description = "Retrieve all inventory stock for a specific store")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved inventory stock"),
        @ApiResponse(responseCode = "404", description = "Store not found")
      })
  public ResponseEntity<List<InventoryStockDto>> getInventoryByStore(
      @Parameter(description = "Store ID", required = true) @PathVariable Long storeId) {
    List<InventoryStockDto> stocks = inventoryStockService.getInventoryByStore(storeId);
    return ResponseEntity.ok(stocks);
  }

  @GetMapping("/medicine/{medicineId}")
  @Operation(
      summary = "Get inventory by medicine",
      description = "Retrieve all stock entries for a specific medicine across all stores")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved inventory stock"),
        @ApiResponse(responseCode = "404", description = "Medicine not found")
      })
  public ResponseEntity<List<InventoryStockDto>> getInventoryByMedicine(
      @Parameter(description = "Medicine ID", required = true) @PathVariable Long medicineId) {
    List<InventoryStockDto> stocks = inventoryStockService.getInventoryByMedicine(medicineId);
    return ResponseEntity.ok(stocks);
  }

  @GetMapping("/store/{storeId}/low-stock")
  @Operation(
      summary = "Get low stock items for a store",
      description = "Retrieve inventory items below reorder level for a specific store")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved low stock items"),
        @ApiResponse(responseCode = "404", description = "Store not found")
      })
  public ResponseEntity<List<InventoryStockDto>> getLowStockItems(
      @Parameter(description = "Store ID", required = true) @PathVariable Long storeId) {
    List<InventoryStockDto> stocks = inventoryStockService.getLowStockItems(storeId);
    return ResponseEntity.ok(stocks);
  }

  @GetMapping("/store/{storeId}/expiring")
  @Operation(
      summary = "Get expiring stock items",
      description = "Retrieve inventory items expiring within specified days for a store")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved expiring stock"),
        @ApiResponse(responseCode = "404", description = "Store not found")
      })
  public ResponseEntity<List<InventoryStockDto>> getExpiringStock(
      @Parameter(description = "Store ID", required = true) @PathVariable Long storeId,
      @Parameter(description = "Days ahead to check for expiry", example = "30")
          @RequestParam(defaultValue = "30")
          int daysAhead) {
    List<InventoryStockDto> stocks = inventoryStockService.getExpiringStock(storeId, daysAhead);
    return ResponseEntity.ok(stocks);
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Delete inventory stock",
      description = "Remove an inventory stock entry from the system")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Inventory stock deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Inventory stock not found")
      })
  public ResponseEntity<Void> deleteInventoryStock(
      @Parameter(description = "Inventory stock ID", required = true) @PathVariable Long id) {
    inventoryStockService.deleteInventoryStock(id);
    return ResponseEntity.noContent().build();
  }
}
