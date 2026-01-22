/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.IngredientStockDto;
import com.pharmacy.dto.IngredientStockPageResponse;
import com.pharmacy.service.IngredientStockService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ingredient-stock")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
    name = "Ingredient Stock Management",
    description = "APIs for managing raw material inventory in stores with batch tracking (v1)")
public class IngredientStockController {

  private final IngredientStockService ingredientStockService;

  @PostMapping
  @Operation(
      summary = "Add new ingredient stock",
      description =
          "Add new ingredient stock to a store with batch tracking and expiry information")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Ingredient stock added successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = IngredientStockDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or duplicate batch"),
        @ApiResponse(responseCode = "404", description = "Store or ingredient not found")
      })
  public ResponseEntity<IngredientStockDto> addIngredientStock(
      @RequestBody IngredientStockDto ingredientStockDto) {
    IngredientStockDto created = ingredientStockService.addIngredientStock(ingredientStockDto);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update ingredient stock",
      description =
          "Update existing ingredient stock information including pricing and reorder levels")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ingredient stock updated successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = IngredientStockDto.class))),
        @ApiResponse(responseCode = "404", description = "Ingredient stock not found")
      })
  public ResponseEntity<IngredientStockDto> updateIngredientStock(
      @Parameter(description = "Ingredient stock ID", required = true) @PathVariable Long id,
      @RequestBody IngredientStockDto ingredientStockDto) {
    IngredientStockDto updated =
        ingredientStockService.updateIngredientStock(id, ingredientStockDto);
    return ResponseEntity.ok(updated);
  }

  @PostMapping("/{id}/adjust")
  @Operation(
      summary = "Adjust ingredient stock quantity",
      description =
          "Adjust stock quantity (positive for addition, negative for reduction) with reason tracking")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Stock adjusted successfully",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Insufficient stock for reduction"),
        @ApiResponse(responseCode = "404", description = "Ingredient stock not found")
      })
  public ResponseEntity<Void> adjustStock(
      @Parameter(description = "Ingredient stock ID", required = true) @PathVariable Long id,
      @Parameter(
              description = "Quantity change (positive to add, negative to reduce)",
              required = true)
          @RequestParam
          BigDecimal quantityChange,
      @Parameter(description = "Reason for adjustment", required = true) @RequestParam
          String reason) {
    ingredientStockService.adjustStock(id, quantityChange, reason);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get ingredient stock by ID",
      description = "Retrieve a specific ingredient stock entry by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ingredient stock found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = IngredientStockDto.class))),
        @ApiResponse(responseCode = "404", description = "Ingredient stock not found")
      })
  public ResponseEntity<IngredientStockDto> getIngredientStockById(
      @Parameter(description = "Ingredient stock ID", required = true) @PathVariable Long id) {
    IngredientStockDto stock = ingredientStockService.getIngredientStockById(id);
    return ResponseEntity.ok(stock);
  }

  @GetMapping
  @Operation(
      summary = "Get all ingredient stock",
      description = "Retrieve all ingredient stock entries across all stores with pagination")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved ingredient stock",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = IngredientStockPageResponse.class)))
  public ResponseEntity<IngredientStockPageResponse> getAllIngredientStock(
      @PageableDefault(size = 20) Pageable pageable) {
    Page<IngredientStockDto> stocks = ingredientStockService.getAllIngredientStock(pageable);
    IngredientStockPageResponse response = new IngredientStockPageResponse();
    response.setContent(stocks.getContent());
    response.setTotalElements(stocks.getTotalElements());
    response.setTotalPages(stocks.getTotalPages());
    response.setNumber(stocks.getNumber());
    response.setSize(stocks.getSize());
    response.setFirst(stocks.isFirst());
    response.setLast(stocks.isLast());
    response.setEmpty(stocks.isEmpty());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/store/{storeId}")
  @Operation(
      summary = "Get ingredient stock by store",
      description = "Retrieve all ingredient stock for a specific store with pagination")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved ingredient stock",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = IngredientStockPageResponse.class))),
        @ApiResponse(responseCode = "404", description = "Store not found")
      })
  public ResponseEntity<IngredientStockPageResponse> getIngredientStockByStore(
      @Parameter(description = "Store ID", required = true) @PathVariable Long storeId,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<IngredientStockDto> stocks =
        ingredientStockService.getIngredientStockByStore(storeId, pageable);
    IngredientStockPageResponse response = new IngredientStockPageResponse();
    response.setContent(stocks.getContent());
    response.setTotalElements(stocks.getTotalElements());
    response.setTotalPages(stocks.getTotalPages());
    response.setNumber(stocks.getNumber());
    response.setSize(stocks.getSize());
    response.setFirst(stocks.isFirst());
    response.setLast(stocks.isLast());
    response.setEmpty(stocks.isEmpty());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/ingredient/{ingredientId}")
  @Operation(
      summary = "Get ingredient stock by ingredient",
      description =
          "Retrieve all stock entries for a specific ingredient across all stores with pagination")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved ingredient stock",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = IngredientStockPageResponse.class))),
        @ApiResponse(responseCode = "404", description = "Ingredient not found")
      })
  public ResponseEntity<IngredientStockPageResponse> getIngredientStockByIngredient(
      @Parameter(description = "Ingredient ID", required = true) @PathVariable Long ingredientId,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<IngredientStockDto> stocks =
        ingredientStockService.getIngredientStockByIngredient(ingredientId, pageable);
    IngredientStockPageResponse response = new IngredientStockPageResponse();
    response.setContent(stocks.getContent());
    response.setTotalElements(stocks.getTotalElements());
    response.setTotalPages(stocks.getTotalPages());
    response.setNumber(stocks.getNumber());
    response.setSize(stocks.getSize());
    response.setFirst(stocks.isFirst());
    response.setLast(stocks.isLast());
    response.setEmpty(stocks.isEmpty());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/store/{storeId}/ingredient/{ingredientId}/total")
  @Operation(
      summary = "Get total quantity of an ingredient in a store",
      description =
          "Calculate total available quantity of a specific ingredient in a specific store")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully calculated total quantity",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Double.class))),
        @ApiResponse(responseCode = "404", description = "Store or ingredient not found")
      })
  public ResponseEntity<Double> getTotalQuantity(
      @Parameter(description = "Store ID", required = true) @PathVariable Long storeId,
      @Parameter(description = "Ingredient ID", required = true) @PathVariable Long ingredientId) {
    Double totalQuantity = ingredientStockService.getTotalQuantity(storeId, ingredientId);
    return ResponseEntity.ok(totalQuantity);
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Delete ingredient stock",
      description = "Remove an ingredient stock entry from the system")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Ingredient stock deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Ingredient stock not found")
      })
  public ResponseEntity<Void> deleteIngredientStock(
      @Parameter(description = "Ingredient stock ID", required = true) @PathVariable Long id) {
    ingredientStockService.deleteIngredientStock(id);
    return ResponseEntity.noContent().build();
  }
}
