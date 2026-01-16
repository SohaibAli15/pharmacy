/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.SaleDto;
import com.pharmacy.entity.Sale;
import com.pharmacy.service.SaleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
    name = "Sales Management",
    description = "APIs for managing sales transactions with FIFO inventory management (v1)")
public class SaleController {

  private final SaleService saleService;

  @PostMapping
  @Operation(
      summary = "Create a new sale",
      description =
          "Create a new sale transaction. Automatically validates stock availability, updates"
              + " inventory using FIFO method, and generates invoice number.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Sale created successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SaleDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or insufficient stock"),
        @ApiResponse(responseCode = "404", description = "Store, customer, or medicine not found")
      })
  public ResponseEntity<SaleDto> createSale(@RequestBody SaleDto saleDto) {
    SaleDto created = saleService.createSale(saleDto);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get sale by ID",
      description = "Retrieve a specific sale transaction by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sale found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SaleDto.class))),
        @ApiResponse(responseCode = "404", description = "Sale not found")
      })
  public ResponseEntity<SaleDto> getSaleById(
      @Parameter(description = "Sale ID", required = true) @PathVariable Long id) {
    SaleDto sale = saleService.getSaleById(id);
    return ResponseEntity.ok(sale);
  }

  @GetMapping("/invoice/{invoiceNumber}")
  @Operation(
      summary = "Get sale by invoice number",
      description = "Retrieve a sale transaction by its invoice number")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Sale found"),
        @ApiResponse(responseCode = "404", description = "Sale not found")
      })
  public ResponseEntity<SaleDto> getSaleByInvoice(
      @Parameter(description = "Invoice number", required = true) @PathVariable
          String invoiceNumber) {
    SaleDto sale = saleService.getSaleByInvoiceNumber(invoiceNumber);
    return ResponseEntity.ok(sale);
  }

  @GetMapping
  @Operation(summary = "Get all sales", description = "Retrieve all sales transactions")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved all sales")
  public ResponseEntity<List<SaleDto>> getAllSales() {
    List<SaleDto> sales = saleService.getAllSales();
    return ResponseEntity.ok(sales);
  }

  @GetMapping("/store/{storeId}")
  @Operation(
      summary = "Get sales by store",
      description = "Retrieve all sales for a specific store")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved sales"),
        @ApiResponse(responseCode = "404", description = "Store not found")
      })
  public ResponseEntity<List<SaleDto>> getSalesByStore(
      @Parameter(description = "Store ID", required = true) @PathVariable Long storeId) {
    List<SaleDto> sales = saleService.getSalesByStore(storeId);
    return ResponseEntity.ok(sales);
  }

  @GetMapping("/customer/{customerId}")
  @Operation(
      summary = "Get sales by customer",
      description = "Retrieve all sales for a specific customer")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved sales"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
      })
  public ResponseEntity<List<SaleDto>> getSalesByCustomer(
      @Parameter(description = "Customer ID", required = true) @PathVariable Long customerId) {
    List<SaleDto> sales = saleService.getSalesByCustomer(customerId);
    return ResponseEntity.ok(sales);
  }

  @GetMapping("/date-range")
  @Operation(
      summary = "Get sales by date range",
      description = "Retrieve sales within a specific date range")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved sales"),
        @ApiResponse(responseCode = "400", description = "Invalid date range")
      })
  public ResponseEntity<List<SaleDto>> getSalesByDateRange(
      @Parameter(description = "Start date (YYYY-MM-DD)", required = true)
          @RequestParam
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate startDate,
      @Parameter(description = "End date (YYYY-MM-DD)", required = true)
          @RequestParam
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate endDate) {
    List<SaleDto> sales = saleService.getSalesByDateRange(startDate, endDate);
    return ResponseEntity.ok(sales);
  }

  @GetMapping("/status/{status}")
  @Operation(
      summary = "Get sales by status",
      description = "Retrieve sales filtered by status (COMPLETED, CANCELLED, RETURNED)")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved sales")
  public ResponseEntity<List<SaleDto>> getSalesByStatus(
      @Parameter(description = "Sale status", required = true) @PathVariable
          Sale.SaleStatus status) {
    List<SaleDto> sales = saleService.getSalesByStatus(status);
    return ResponseEntity.ok(sales);
  }

  @PostMapping("/{id}/cancel")
  @Operation(
      summary = "Cancel a sale",
      description = "Cancel a sale and restore inventory stock to the store")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Sale cancelled successfully"),
        @ApiResponse(responseCode = "404", description = "Sale not found"),
        @ApiResponse(responseCode = "400", description = "Sale is already cancelled")
      })
  public ResponseEntity<SaleDto> cancelSale(
      @Parameter(description = "Sale ID", required = true) @PathVariable Long id) {
    SaleDto cancelled = saleService.cancelSale(id);
    return ResponseEntity.ok(cancelled);
  }

  @PostMapping("/{id}/return")
  @Operation(
      summary = "Return a sale",
      description = "Process a sale return and restore inventory stock to the store")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Sale returned successfully"),
        @ApiResponse(responseCode = "404", description = "Sale not found"),
        @ApiResponse(responseCode = "400", description = "Sale is already returned")
      })
  public ResponseEntity<SaleDto> returnSale(
      @Parameter(description = "Sale ID", required = true) @PathVariable Long id) {
    SaleDto returned = saleService.returnSale(id);
    return ResponseEntity.ok(returned);
  }
}
