/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.PrescriptionDto;
import com.pharmacy.service.PrescriptionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/prescriptions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
    name = "Prescription Management",
    description = "APIs for managing medical prescriptions and prescription items (v1)")
public class PrescriptionController {

  private final PrescriptionService prescriptionService;

  @PostMapping
  @Operation(
      summary = "Create a new prescription",
      description = "Create a new prescription with prescribed medicines and dosage instructions")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Prescription created successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PrescriptionDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Customer or doctor not found")
      })
  public ResponseEntity<PrescriptionDto> createPrescription(
      @RequestBody PrescriptionDto prescriptionDto) {
    PrescriptionDto created = prescriptionService.createPrescription(prescriptionDto);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get prescription by ID",
      description = "Retrieve a specific prescription by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Prescription found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PrescriptionDto.class))),
        @ApiResponse(responseCode = "404", description = "Prescription not found")
      })
  public ResponseEntity<PrescriptionDto> getPrescriptionById(
      @Parameter(description = "Prescription ID", required = true) @PathVariable Long id) {
    PrescriptionDto prescription = prescriptionService.getPrescriptionById(id);
    return ResponseEntity.ok(prescription);
  }

  @GetMapping
  @Operation(
      summary = "Get all prescriptions",
      description = "Retrieve all prescriptions in the system with pagination")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved prescriptions",
      content =
          @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
  public ResponseEntity<Page<PrescriptionDto>> getAllPrescriptions(
      @PageableDefault(size = 20) Pageable pageable) {
    Page<PrescriptionDto> prescriptions = prescriptionService.getAllPrescriptions(pageable);
    return ResponseEntity.ok(prescriptions);
  }

  @GetMapping("/customer/{customerId}")
  @Operation(
      summary = "Get prescriptions by customer",
      description = "Retrieve all prescriptions for a specific customer with pagination")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved prescriptions",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "404", description = "Customer not found")
      })
  public ResponseEntity<Page<PrescriptionDto>> getPrescriptionsByCustomer(
      @Parameter(description = "Customer ID", required = true) @PathVariable Long customerId,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<PrescriptionDto> prescriptions =
        prescriptionService.getPrescriptionsByCustomer(customerId, pageable);
    return ResponseEntity.ok(prescriptions);
  }

  @GetMapping("/status/{status}")
  @Operation(
      summary = "Get prescriptions by status",
      description =
          "Retrieve prescriptions filtered by status (e.g., PENDING, DISPENSED, EXPIRED) with pagination")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved prescriptions",
      content =
          @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
  public ResponseEntity<Page<PrescriptionDto>> getPrescriptionsByStatus(
      @Parameter(description = "Prescription status", required = true) @PathVariable String status,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<PrescriptionDto> prescriptions =
        prescriptionService.getPrescriptionsByStatus(status, pageable);
    return ResponseEntity.ok(prescriptions);
  }

  @PutMapping("/{id}/status")
  @Operation(
      summary = "Update prescription status",
      description = "Update the status of a prescription (e.g., mark as DISPENSED)")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Status updated successfully"),
        @ApiResponse(responseCode = "404", description = "Prescription not found"),
        @ApiResponse(responseCode = "400", description = "Invalid status")
      })
  public ResponseEntity<PrescriptionDto> updatePrescriptionStatus(
      @Parameter(description = "Prescription ID", required = true) @PathVariable Long id,
      @Parameter(description = "New status", required = true) @RequestParam String status) {
    PrescriptionDto updated = prescriptionService.updatePrescriptionStatus(id, status);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Delete a prescription",
      description = "Delete a prescription from the system")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Prescription deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Prescription not found")
      })
  public ResponseEntity<Void> deletePrescription(
      @Parameter(description = "Prescription ID", required = true) @PathVariable Long id) {
    prescriptionService.deletePrescription(id);
    return ResponseEntity.noContent().build();
  }
}
