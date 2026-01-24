/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmacy.service.CustomerService;
import com.pharmacy.service.SupplierService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Partners Dashboard", description = "Endpoints for partners dashboard summary metrics")
@RestController
@RequestMapping("/api/v1/partners-dashboard")
@RequiredArgsConstructor
public class PartnersDashboardController {
  private final SupplierService supplierService;
  private final CustomerService customerService;

  @Operation(
      summary = "Get partners dashboard summary",
      description =
          "Returns summary metrics for the partners dashboard (total suppliers, total customers, supplier outstanding, customer outstanding)")
  @ApiResponse(
      responseCode = "200",
      description = "Dashboard summary returned",
      content =
          @Content(
              mediaType = "application/json",
              schema =
                  @Schema(
                      type = "object",
                      example =
                          "{\"totalSuppliers\":3,\"totalCustomers\":3,\"supplierOutstanding\":120000,\"customerOutstanding\":237000}",
                      implementation = java.util.Map.class)))
  @GetMapping("/summary")
  public ResponseEntity<Map<String, Object>> getSummary() {
    Map<String, Object> summary = new HashMap<>();
    var suppliers = supplierService.getAllSuppliers();
    var customers = customerService.getAllCustomers();
    summary.put("totalSuppliers", suppliers.size());
    summary.put("totalCustomers", customers.size());
    summary.put(
        "supplierOutstanding",
        suppliers.stream()
            .map(
                s ->
                    s.getOutstandingAmount() != null
                        ? s.getOutstandingAmount()
                        : java.math.BigDecimal.ZERO)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add));
    summary.put(
        "customerOutstanding",
        customers.stream()
            .map(
                c ->
                    c.getOutstandingAmount() != null
                        ? c.getOutstandingAmount()
                        : java.math.BigDecimal.ZERO)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add));
    return ResponseEntity.ok(summary);
  }
}
