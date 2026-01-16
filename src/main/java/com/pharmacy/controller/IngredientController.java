/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.IngredientDto;
import com.pharmacy.service.IngredientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ingredients")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
    name = "Ingredient Management",
    description = "APIs for managing raw material ingredients and ingredient inventory (v1)")
public class IngredientController {

  private final IngredientService ingredientService;

  @GetMapping
  @Operation(
      summary = "Get all ingredients",
      description = "Retrieve all ingredients in the system")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved all ingredients")
  public List<IngredientDto> listAll() {
    return ingredientService.listAll();
  }

  @GetMapping("/low-stock")
  @Operation(
      summary = "Get low stock ingredients",
      description = "Retrieve ingredients with stock below reorder level")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved low stock ingredients")
  public List<IngredientDto> getLowStock() {
    return ingredientService.getLowStockIngredients();
  }

  @GetMapping("/high-stock")
  @Operation(
      summary = "Get high stock ingredients",
      description = "Retrieve ingredients with stock above maximum level")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved high stock ingredients")
  public List<IngredientDto> getHighStock() {
    return ingredientService.getHighStockIngredients();
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get ingredient by ID",
      description = "Retrieve a specific ingredient by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ingredient found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = IngredientDto.class))),
        @ApiResponse(responseCode = "404", description = "Ingredient not found")
      })
  public ResponseEntity<IngredientDto> get(
      @Parameter(description = "Ingredient ID", required = true) @PathVariable Long id) {
    IngredientDto dto = ingredientService.getById(id);
    if (dto == null) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(dto);
  }

  @PostMapping
  @Operation(
      summary = "Create a new ingredient",
      description = "Register a new ingredient with specifications and stock levels")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ingredient created successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = IngredientDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
      })
  public ResponseEntity<IngredientDto> create(@RequestBody IngredientDto dto) {
    IngredientDto created = ingredientService.create(dto);
    return ResponseEntity.ok(created);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update an ingredient",
      description = "Update ingredient information including specifications and stock levels")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Ingredient updated successfully"),
        @ApiResponse(responseCode = "404", description = "Ingredient not found")
      })
  public ResponseEntity<IngredientDto> update(
      @Parameter(description = "Ingredient ID", required = true) @PathVariable Long id,
      @RequestBody IngredientDto dto) {
    IngredientDto updated = ingredientService.update(id, dto);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete an ingredient", description = "Remove an ingredient from the system")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Ingredient deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Ingredient not found")
      })
  public ResponseEntity<Void> delete(
      @Parameter(description = "Ingredient ID", required = true) @PathVariable Long id) {
    ingredientService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
