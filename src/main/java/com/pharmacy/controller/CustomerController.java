/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.CustomerDto;
import com.pharmacy.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
    name = "Customer Management",
    description = "APIs for managing customers with medical history and contact information (v1)")
public class CustomerController {

  private final CustomerService customerService;

  @PostMapping
  @Operation(
      summary = "Create a new customer",
      description = "Register a new customer with contact details and medical history")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Customer created successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CustomerDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or duplicate customer code")
      })
  public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto) {
    CustomerDto created = customerService.createCustomer(customerDto);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update an existing customer",
      description = "Update customer information including contact details and medical history")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
      })
  public ResponseEntity<CustomerDto> updateCustomer(
      @Parameter(description = "Customer ID", required = true) @PathVariable Long id,
      @RequestBody CustomerDto customerDto) {
    CustomerDto updated = customerService.updateCustomer(id, customerDto);
    return ResponseEntity.ok(updated);
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get customer by ID",
      description = "Retrieve a specific customer by their ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Customer found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CustomerDto.class))),
        @ApiResponse(responseCode = "404", description = "Customer not found")
      })
  public ResponseEntity<CustomerDto> getCustomerById(
      @Parameter(description = "Customer ID", required = true) @PathVariable Long id) {
    CustomerDto customer = customerService.getCustomerById(id);
    return ResponseEntity.ok(customer);
  }

  @GetMapping("/code/{code}")
  @Operation(
      summary = "Get customer by code",
      description = "Retrieve a customer by their unique customer code")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Customer found"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
      })
  public ResponseEntity<CustomerDto> getCustomerByCode(
      @Parameter(description = "Customer code", required = true) @PathVariable String code) {
    CustomerDto customer = customerService.getCustomerByCode(code);
    return ResponseEntity.ok(customer);
  }

  @GetMapping
  @Operation(summary = "Get all customers", description = "Retrieve all registered customers")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved all customers")
  public ResponseEntity<List<CustomerDto>> getAllCustomers() {
    List<CustomerDto> customers = customerService.getAllCustomers();
    return ResponseEntity.ok(customers);
  }

  @GetMapping("/search")
  @Operation(
      summary = "Search customers by name",
      description = "Search for customers by first name, last name, or phone number")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved matching customers")
  public ResponseEntity<List<CustomerDto>> searchCustomers(
      @Parameter(description = "Search term (name or phone)", required = true) @RequestParam
          String searchTerm) {
    List<CustomerDto> customers = customerService.searchCustomers(searchTerm);
    return ResponseEntity.ok(customers);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a customer", description = "Remove a customer from the system")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
      })
  public ResponseEntity<Void> deleteCustomer(
      @Parameter(description = "Customer ID", required = true) @PathVariable Long id) {
    customerService.deleteCustomer(id);
    return ResponseEntity.noContent().build();
  }
}
