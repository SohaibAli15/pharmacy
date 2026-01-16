/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.PurchaseOrderDto;
import com.pharmacy.dto.PurchaseOrderItemDto;
import com.pharmacy.entity.PurchaseOrder;
import com.pharmacy.service.PurchaseOrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/purchase-orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
    name = "Purchase Order Management",
    description = "APIs for managing purchase orders from suppliers with receiving workflow (v1)")
public class PurchaseOrderController {

  private final PurchaseOrderService purchaseOrderService;

  @PostMapping
  @Operation(
      summary = "Create a new purchase order",
      description = "Create a new purchase order from a supplier with ordered items")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Purchase order created successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PurchaseOrderDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Supplier not found")
      })
  public ResponseEntity<PurchaseOrderDto> createPurchaseOrder(
      @RequestBody PurchaseOrderDto purchaseOrderDto) {
    PurchaseOrderDto created = purchaseOrderService.createPurchaseOrder(purchaseOrderDto);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @PutMapping("/{id}/status")
  @Operation(
      summary = "Update purchase order status",
      description = "Update the status of a purchase order (APPROVED, REJECTED, etc.)")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Status updated successfully"),
        @ApiResponse(responseCode = "404", description = "Purchase order not found"),
        @ApiResponse(responseCode = "400", description = "Invalid status transition")
      })
  public ResponseEntity<PurchaseOrderDto> updateOrderStatus(
      @Parameter(description = "Purchase order ID", required = true) @PathVariable Long id,
      @Parameter(description = "New status", required = true) @RequestParam
          PurchaseOrder.OrderStatus status) {
    PurchaseOrderDto updated = purchaseOrderService.updateOrderStatus(id, status);
    return ResponseEntity.ok(updated);
  }

  @PostMapping("/{id}/receive")
  @Operation(
      summary = "Receive items from purchase order",
      description = "Process the receipt of items from a purchase order with quality checks")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Items received successfully"),
        @ApiResponse(responseCode = "404", description = "Purchase order not found"),
        @ApiResponse(responseCode = "400", description = "Invalid received quantities")
      })
  public ResponseEntity<PurchaseOrderDto> receiveItems(
      @Parameter(description = "Purchase order ID", required = true) @PathVariable Long id,
      @RequestBody List<PurchaseOrderItemDto> receivedItems) {
    PurchaseOrderDto updated = purchaseOrderService.receiveItems(id, receivedItems);
    return ResponseEntity.ok(updated);
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get purchase order by ID",
      description = "Retrieve a specific purchase order by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Purchase order found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PurchaseOrderDto.class))),
        @ApiResponse(responseCode = "404", description = "Purchase order not found")
      })
  public ResponseEntity<PurchaseOrderDto> getPurchaseOrderById(
      @Parameter(description = "Purchase order ID", required = true) @PathVariable Long id) {
    PurchaseOrderDto order = purchaseOrderService.getPurchaseOrderById(id);
    return ResponseEntity.ok(order);
  }

  @GetMapping
  @Operation(
      summary = "Get all purchase orders",
      description = "Retrieve all purchase orders in the system")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved all purchase orders")
  public ResponseEntity<List<PurchaseOrderDto>> getAllPurchaseOrders() {
    List<PurchaseOrderDto> orders = purchaseOrderService.getAllPurchaseOrders();
    return ResponseEntity.ok(orders);
  }

  @GetMapping("/status/{status}")
  @Operation(
      summary = "Get purchase orders by status",
      description = "Retrieve purchase orders filtered by status")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved purchase orders")
  public ResponseEntity<List<PurchaseOrderDto>> getPurchaseOrdersByStatus(
      @Parameter(description = "Order status", required = true) @PathVariable
          PurchaseOrder.OrderStatus status) {
    List<PurchaseOrderDto> orders = purchaseOrderService.getPurchaseOrdersByStatus(status);
    return ResponseEntity.ok(orders);
  }

  @GetMapping("/supplier/{supplierId}")
  @Operation(
      summary = "Get purchase orders by supplier",
      description = "Retrieve all purchase orders for a specific supplier")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved purchase orders"),
        @ApiResponse(responseCode = "404", description = "Supplier not found")
      })
  public ResponseEntity<List<PurchaseOrderDto>> getPurchaseOrdersBySupplier(
      @Parameter(description = "Supplier ID", required = true) @PathVariable Long supplierId) {
    List<PurchaseOrderDto> orders = purchaseOrderService.getPurchaseOrdersBySupplier(supplierId);
    return ResponseEntity.ok(orders);
  }
}
