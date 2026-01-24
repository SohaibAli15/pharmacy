/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.EmployeeDto;
import com.pharmacy.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Employee Management", description = "Endpoints for managing employees")
@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {
  private final EmployeeService employeeService;

  @Operation(summary = "Create a new employee", description = "Creates a new employee record")
  @ApiResponse(
      responseCode = "200",
      description = "Employee created",
      content = @Content(schema = @Schema(implementation = EmployeeDto.class)))
  @PostMapping
  public ResponseEntity<EmployeeDto> create(@RequestBody EmployeeDto dto) {
    return ResponseEntity.ok(employeeService.create(dto));
  }

  @Operation(summary = "Update an employee", description = "Updates an existing employee record")
  @ApiResponse(
      responseCode = "200",
      description = "Employee updated",
      content = @Content(schema = @Schema(implementation = EmployeeDto.class)))
  @PutMapping("/{id}")
  public ResponseEntity<EmployeeDto> update(@PathVariable Long id, @RequestBody EmployeeDto dto) {
    return ResponseEntity.ok(employeeService.update(id, dto));
  }

  @Operation(summary = "Delete an employee", description = "Deletes an employee by ID")
  @ApiResponse(responseCode = "204", description = "Employee deleted")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    employeeService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Get an employee by ID", description = "Fetches an employee by their ID")
  @ApiResponse(
      responseCode = "200",
      description = "Employee found",
      content = @Content(schema = @Schema(implementation = EmployeeDto.class)))
  @GetMapping("/{id}")
  public ResponseEntity<EmployeeDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(employeeService.getById(id));
  }

  @Operation(
      summary = "Get an employee by Employee ID",
      description = "Fetches an employee by their Employee ID")
  @ApiResponse(
      responseCode = "200",
      description = "Employee found",
      content = @Content(schema = @Schema(implementation = EmployeeDto.class)))
  @GetMapping("/empid/{employeeId}")
  public ResponseEntity<EmployeeDto> getByEmployeeId(@PathVariable String employeeId) {
    return ResponseEntity.ok(employeeService.getByEmployeeId(employeeId));
  }

  @Operation(summary = "List all employees", description = "Fetches all employees")
  @ApiResponse(
      responseCode = "200",
      description = "List of employees",
      content = @Content(schema = @Schema(implementation = EmployeeDto.class)))
  @GetMapping
  public ResponseEntity<List<EmployeeDto>> getAll() {
    return ResponseEntity.ok(employeeService.getAll());
  }
}
