/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.LeaveRequestDto;
import com.pharmacy.service.LeaveManagementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Leave Management", description = "Endpoints for managing employee leave requests")
@RestController
@RequestMapping("/api/v1/leave")
@RequiredArgsConstructor
public class LeaveController {
  private final LeaveManagementService leaveService;

  @Operation(summary = "Create leave request", description = "Creates a new leave request")
  @ApiResponse(
      responseCode = "200",
      description = "Leave request created",
      content = @Content(schema = @Schema(implementation = LeaveRequestDto.class)))
  @PostMapping
  public ResponseEntity<LeaveRequestDto> create(@RequestBody LeaveRequestDto dto) {
    return ResponseEntity.ok(leaveService.create(dto));
  }

  @Operation(summary = "Update leave request", description = "Updates an existing leave request")
  @ApiResponse(
      responseCode = "200",
      description = "Leave request updated",
      content = @Content(schema = @Schema(implementation = LeaveRequestDto.class)))
  @PutMapping("/{id}")
  public ResponseEntity<LeaveRequestDto> update(
      @PathVariable Long id, @RequestBody LeaveRequestDto dto) {
    return ResponseEntity.ok(leaveService.update(id, dto));
  }

  @Operation(summary = "Delete leave request", description = "Deletes a leave request by ID")
  @ApiResponse(responseCode = "204", description = "Leave request deleted")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    leaveService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Get leave request by ID", description = "Fetches a leave request by its ID")
  @ApiResponse(
      responseCode = "200",
      description = "Leave request found",
      content = @Content(schema = @Schema(implementation = LeaveRequestDto.class)))
  @GetMapping("/{id}")
  public ResponseEntity<LeaveRequestDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(leaveService.getById(id));
  }

  @Operation(summary = "List all leave requests", description = "Fetches all leave requests")
  @ApiResponse(
      responseCode = "200",
      description = "List of leave requests",
      content = @Content(schema = @Schema(implementation = LeaveRequestDto.class)))
  @GetMapping
  public ResponseEntity<List<LeaveRequestDto>> getAll() {
    return ResponseEntity.ok(leaveService.getAll());
  }
}
