/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.StockTransferDto;
import com.pharmacy.dto.StockTransferItemDto;
import com.pharmacy.service.StockTransferService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/stock-transfers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
    name = "Stock Transfer Management",
    description = "APIs for managing stock transfers between stores with approval workflow (v1)")
public class StockTransferController {

  private final StockTransferService stockTransferService;

  @PostMapping
  @Operation(summary = "Create a new stock transfer request")
  public ResponseEntity<StockTransferDto> createStockTransfer(
      @RequestBody StockTransferDto stockTransferDto) {
    StockTransferDto created = stockTransferService.createStockTransfer(stockTransferDto);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @PostMapping("/{id}/approve")
  @Operation(summary = "Approve a stock transfer")
  public ResponseEntity<StockTransferDto> approveStockTransfer(
      @PathVariable Long id, @RequestParam Long approvedById) {
    StockTransferDto approved = stockTransferService.approveStockTransfer(id, approvedById);
    return ResponseEntity.ok(approved);
  }

  @PostMapping("/{id}/dispatch")
  @Operation(summary = "Dispatch approved stock transfer")
  public ResponseEntity<StockTransferDto> dispatchStockTransfer(@PathVariable Long id) {
    StockTransferDto dispatched = stockTransferService.dispatchStockTransfer(id);
    return ResponseEntity.ok(dispatched);
  }

  @PostMapping("/{id}/receive")
  @Operation(summary = "Receive stock transfer at destination")
  public ResponseEntity<StockTransferDto> receiveStockTransfer(
      @PathVariable Long id,
      @RequestParam Long receivedById,
      @RequestBody List<StockTransferItemDto> receivedItems) {
    StockTransferDto received =
        stockTransferService.receiveStockTransfer(id, receivedById, receivedItems);
    return ResponseEntity.ok(received);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get stock transfer by ID")
  public ResponseEntity<StockTransferDto> getStockTransferById(@PathVariable Long id) {
    StockTransferDto transfer = stockTransferService.getStockTransferById(id);
    return ResponseEntity.ok(transfer);
  }

  @GetMapping
  @Operation(summary = "Get all stock transfers")
  public ResponseEntity<List<StockTransferDto>> getAllStockTransfers() {
    List<StockTransferDto> transfers = stockTransferService.getAllStockTransfers();
    return ResponseEntity.ok(transfers);
  }

  @GetMapping("/store/{storeId}")
  @Operation(summary = "Get stock transfers for a specific store")
  public ResponseEntity<List<StockTransferDto>> getStockTransfersByStore(
      @PathVariable Long storeId) {
    List<StockTransferDto> transfers = stockTransferService.getStockTransfersByStore(storeId);
    return ResponseEntity.ok(transfers);
  }
}
