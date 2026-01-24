/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.PurchaseOrderDto;
import com.pharmacy.dto.PurchaseOrderItemDto;
import com.pharmacy.dto.PurchaseOrderPageResponse;
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

@Tag(name = "Purchase Orders", description = "Endpoints for managing Purchase Orders")
@RestController
@RequestMapping("/api/v1/purchase-orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PurchaseOrderController {

  private final PurchaseOrderService purchaseOrderService;

  @Operation(
      summary = "Create a new purchase order",
      description = "Create a new purchase order from a supplier with ordered items")
  @ApiResponse(
      responseCode = "201",
      description = "Purchase order created successfully",
      content = @Content(schema = @Schema(implementation = PurchaseOrderDto.class)))
  @PostMapping
  public ResponseEntity<PurchaseOrderDto> createPurchaseOrder(
      @RequestBody PurchaseOrderDto purchaseOrderDto) {
    PurchaseOrderDto created = purchaseOrderService.createPurchaseOrder(purchaseOrderDto);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @PutMapping("/{id}/status")
  @Operation(
      summary = "Update purchase order status",
      description = "Update the status of a purchase order (APPROVED, REJECTED, etc.)")
  @ApiResponse(
      responseCode = "200",
      description = "Status updated successfully",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = PurchaseOrderDto.class)))
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
  @ApiResponse(
      responseCode = "200",
      description = "Items received successfully",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = PurchaseOrderDto.class)))
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
  @ApiResponse(
      responseCode = "200",
      description = "Purchase order found",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = PurchaseOrderDto.class)))
  public ResponseEntity<PurchaseOrderDto> getPurchaseOrderById(
      @Parameter(description = "Purchase order ID", required = true) @PathVariable Long id) {
    PurchaseOrderDto order = purchaseOrderService.getPurchaseOrderById(id);
    return ResponseEntity.ok(order);
  }

  @GetMapping
  @Operation(
      summary = "Get all purchase orders",
      description = "Retrieve all purchase orders in the system with pagination")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved purchase orders",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = PurchaseOrderPageResponse.class)))
  public ResponseEntity<PurchaseOrderPageResponse> getAllPurchaseOrders(
      @PageableDefault(size = 20) Pageable pageable) {
    Page<PurchaseOrderDto> orders = purchaseOrderService.getAllPurchaseOrders(pageable);
    PurchaseOrderPageResponse response = new PurchaseOrderPageResponse();
    response.setContent(orders.getContent());
    response.setTotalElements(orders.getTotalElements());
    response.setTotalPages(orders.getTotalPages());
    response.setNumber(orders.getNumber());
    response.setSize(orders.getSize());
    response.setFirst(orders.isFirst());
    response.setLast(orders.isLast());
    response.setEmpty(orders.isEmpty());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/status/{status}")
  @Operation(
      summary = "Get purchase orders by status",
      description = "Retrieve purchase orders filtered by status with pagination")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved purchase orders",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = PurchaseOrderPageResponse.class)))
  public ResponseEntity<PurchaseOrderPageResponse> getPurchaseOrdersByStatus(
      @Parameter(description = "Order status", required = true) @PathVariable
          PurchaseOrder.OrderStatus status,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<PurchaseOrderDto> orders =
        purchaseOrderService.getPurchaseOrdersByStatus(status, pageable);
    PurchaseOrderPageResponse response = new PurchaseOrderPageResponse();
    response.setContent(orders.getContent());
    response.setTotalElements(orders.getTotalElements());
    response.setTotalPages(orders.getTotalPages());
    response.setNumber(orders.getNumber());
    response.setSize(orders.getSize());
    response.setFirst(orders.isFirst());
    response.setLast(orders.isLast());
    response.setEmpty(orders.isEmpty());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/supplier/{supplierId}")
  @Operation(
      summary = "Get purchase orders by supplier",
      description = "Retrieve all purchase orders for a specific supplier with pagination")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved purchase orders",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PurchaseOrderPageResponse.class))),
        @ApiResponse(responseCode = "404", description = "Supplier not found")
      })
  public ResponseEntity<PurchaseOrderPageResponse> getPurchaseOrdersBySupplier(
      @Parameter(description = "Supplier ID", required = true) @PathVariable Long supplierId,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<PurchaseOrderDto> orders =
        purchaseOrderService.getPurchaseOrdersBySupplier(supplierId, pageable);
    PurchaseOrderPageResponse response = new PurchaseOrderPageResponse();
    response.setContent(orders.getContent());
    response.setTotalElements(orders.getTotalElements());
    response.setTotalPages(orders.getTotalPages());
    response.setNumber(orders.getNumber());
    response.setSize(orders.getSize());
    response.setFirst(orders.isFirst());
    response.setLast(orders.isLast());
    response.setEmpty(orders.isEmpty());
    return ResponseEntity.ok(response);
  }
}
