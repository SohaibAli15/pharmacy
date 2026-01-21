/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.MedicineDto;
import com.pharmacy.dto.MedicinePageResponse;
import com.pharmacy.service.MedicineService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/medicines")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
    name = "Medicine Management",
    description = "APIs for managing finished medicine products (v1)")
public class MedicineController {

  private final MedicineService medicineService;

  @GetMapping
  @Operation(
      summary = "Get all medicines",
      description = "Retrieve all medicines in the catalog with pagination")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved medicines",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = MedicinePageResponse.class)))
  public ResponseEntity<MedicinePageResponse> getAllMedicines(
      @PageableDefault(size = 20) Pageable pageable) {
    Page<MedicineDto> medicines = medicineService.listAll(pageable);
    MedicinePageResponse response = new MedicinePageResponse();
    response.setContent(medicines.getContent());
    response.setTotalElements(medicines.getTotalElements());
    response.setTotalPages(medicines.getTotalPages());
    response.setNumber(medicines.getNumber());
    response.setSize(medicines.getSize());
    response.setFirst(medicines.isFirst());
    response.setLast(medicines.isLast());
    response.setEmpty(medicines.isEmpty());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/search")
  @Operation(summary = "Search medicines", description = "Search medicines by name with pagination")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved matching medicines",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = MedicinePageResponse.class)))
  public ResponseEntity<MedicinePageResponse> searchMedicines(
      @Parameter(description = "Search query", required = true) @RequestParam("q") String q,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<MedicineDto> medicines = medicineService.searchByName(q, pageable);
    MedicinePageResponse response = new MedicinePageResponse();
    response.setContent(medicines.getContent());
    response.setTotalElements(medicines.getTotalElements());
    response.setTotalPages(medicines.getTotalPages());
    response.setNumber(medicines.getNumber());
    response.setSize(medicines.getSize());
    response.setFirst(medicines.isFirst());
    response.setLast(medicines.isLast());
    response.setEmpty(medicines.isEmpty());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/category/{category}")
  @Operation(
      summary = "Get medicines by category",
      description = "Retrieve medicines filtered by category with pagination")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved medicines by category",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = MedicinePageResponse.class)))
  public ResponseEntity<MedicinePageResponse> getMedicinesByCategory(
      @Parameter(description = "Medicine category", required = true) @PathVariable String category,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<MedicineDto> medicines = medicineService.findByCategory(category, pageable);
    MedicinePageResponse response = new MedicinePageResponse();
    response.setContent(medicines.getContent());
    response.setTotalElements(medicines.getTotalElements());
    response.setTotalPages(medicines.getTotalPages());
    response.setNumber(medicines.getNumber());
    response.setSize(medicines.getSize());
    response.setFirst(medicines.isFirst());
    response.setLast(medicines.isLast());
    response.setEmpty(medicines.isEmpty());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get medicine by ID", description = "Retrieve a specific medicine by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Medicine found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MedicineDto.class))),
        @ApiResponse(responseCode = "404", description = "Medicine not found")
      })
  public ResponseEntity<MedicineDto> getMedicineById(
      @Parameter(description = "Medicine ID", required = true) @PathVariable Long id) {
    MedicineDto dto = medicineService.getById(id);
    if (dto == null) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(dto);
  }

  @PostMapping
  @Operation(
      summary = "Create a new medicine",
      description = "Register a new medicine product with specifications and pricing")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Medicine created successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MedicineDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
      })
  public ResponseEntity<MedicineDto> createMedicine(@RequestBody MedicineDto dto) {
    MedicineDto created = medicineService.create(dto);
    return ResponseEntity.ok(created);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update a medicine",
      description = "Update medicine information including specifications and pricing")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Medicine updated successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MedicineDto.class))),
        @ApiResponse(responseCode = "404", description = "Medicine not found")
      })
  public ResponseEntity<MedicineDto> updateMedicine(
      @Parameter(description = "Medicine ID", required = true) @PathVariable Long id,
      @RequestBody MedicineDto dto) {
    MedicineDto updated = medicineService.update(id, dto);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a medicine", description = "Remove a medicine from the catalog")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Medicine deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Medicine not found")
      })
  public ResponseEntity<Void> deleteMedicine(
      @Parameter(description = "Medicine ID", required = true) @PathVariable Long id) {
    medicineService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
