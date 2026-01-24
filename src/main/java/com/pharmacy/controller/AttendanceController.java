/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.AttendanceDto;
import com.pharmacy.service.AttendanceManagementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Attendance Management", description = "Endpoints for managing employee attendance")
@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {
  private final AttendanceManagementService attendanceService;

  @Operation(summary = "Create attendance record", description = "Creates a new attendance record")
  @ApiResponse(
      responseCode = "200",
      description = "Attendance created",
      content = @Content(schema = @Schema(implementation = AttendanceDto.class)))
  @PostMapping
  public ResponseEntity<AttendanceDto> create(@RequestBody AttendanceDto dto) {
    return ResponseEntity.ok(attendanceService.create(dto));
  }

  @Operation(
      summary = "Update attendance record",
      description = "Updates an existing attendance record")
  @ApiResponse(
      responseCode = "200",
      description = "Attendance updated",
      content = @Content(schema = @Schema(implementation = AttendanceDto.class)))
  @PutMapping("/{id}")
  public ResponseEntity<AttendanceDto> update(
      @PathVariable Long id, @RequestBody AttendanceDto dto) {
    return ResponseEntity.ok(attendanceService.update(id, dto));
  }

  @Operation(
      summary = "Delete attendance record",
      description = "Deletes an attendance record by ID")
  @ApiResponse(responseCode = "204", description = "Attendance deleted")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    attendanceService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Get attendance by ID",
      description = "Fetches an attendance record by its ID")
  @ApiResponse(
      responseCode = "200",
      description = "Attendance found",
      content = @Content(schema = @Schema(implementation = AttendanceDto.class)))
  @GetMapping("/{id}")
  public ResponseEntity<AttendanceDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(attendanceService.getById(id));
  }

  @Operation(
      summary = "List all attendance records",
      description = "Fetches all attendance records")
  @ApiResponse(
      responseCode = "200",
      description = "List of attendance records",
      content = @Content(schema = @Schema(implementation = AttendanceDto.class)))
  @GetMapping
  public ResponseEntity<List<AttendanceDto>> getAll() {
    return ResponseEntity.ok(attendanceService.getAll());
  }
}
