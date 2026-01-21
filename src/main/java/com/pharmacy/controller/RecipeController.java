/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.ErrorResponse;
import com.pharmacy.dto.RecipeDto;
import com.pharmacy.dto.RecipePageResponse;
import com.pharmacy.dto.RecipeStatsResponse;
import com.pharmacy.service.RecipeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/recipes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
    name = "Recipe Management",
    description = "APIs for managing pharmaceutical recipes and formulations (v1)")
public class RecipeController {

  private final RecipeService recipeService;

  @Operation(
      summary = "Get all recipes",
      description =
          "Retrieve all recipes in the system with optional filtering for active recipes with pagination")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved recipes",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RecipePageResponse.class)))
      })
  @GetMapping
  public ResponseEntity<RecipePageResponse> getAllRecipes(
      @Parameter(description = "Filter for active recipes only") @RequestParam(required = false)
          Boolean activeOnly,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<RecipeDto> recipes =
        activeOnly != null && activeOnly
            ? recipeService.listAllActive(pageable)
            : recipeService.listAll(pageable);
    RecipePageResponse response = new RecipePageResponse();
    response.setContent(recipes.getContent());
    response.setTotalElements(recipes.getTotalElements());
    response.setTotalPages(recipes.getTotalPages());
    response.setNumber(recipes.getNumber());
    response.setSize(recipes.getSize());
    response.setFirst(recipes.isFirst());
    response.setLast(recipes.isLast());
    response.setEmpty(recipes.isEmpty());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/search")
  @Operation(
      summary = "Search recipes",
      description =
          "Search recipes using multiple filter criteria including name, category, and product with pagination")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved filtered recipes",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RecipePageResponse.class)))
      })
  public ResponseEntity<RecipePageResponse> searchRecipes(
      @Parameter(description = "Search query for recipe name") @RequestParam(required = false)
          String q,
      @Parameter(description = "Filter by category") @RequestParam(required = false)
          String category,
      @Parameter(description = "Filter by sub-category") @RequestParam(required = false)
          String subCategory,
      @Parameter(description = "Filter by status (ACTIVE, DRAFT, ARCHIVED)")
          @RequestParam(required = false)
          String status,
      @Parameter(description = "Filter by product ID") @RequestParam(required = false)
          Long productId,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<RecipeDto> recipes =
        recipeService.searchRecipes(category, subCategory, status, productId, q, pageable);
    RecipePageResponse response = new RecipePageResponse();
    response.setContent(recipes.getContent());
    response.setTotalElements(recipes.getTotalElements());
    response.setTotalPages(recipes.getTotalPages());
    response.setNumber(recipes.getNumber());
    response.setSize(recipes.getSize());
    response.setFirst(recipes.isFirst());
    response.setLast(recipes.isLast());
    response.setEmpty(recipes.isEmpty());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/category/{category}")
  @Operation(
      summary = "Get recipes by category",
      description = "Retrieve all recipes belonging to a specific category with pagination")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved recipes by category",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RecipePageResponse.class)))
      })
  public ResponseEntity<RecipePageResponse> getRecipesByCategory(
      @Parameter(description = "Category name", example = "Packaging Material") @PathVariable
          String category,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<RecipeDto> recipes = recipeService.findByCategory(category, pageable);
    RecipePageResponse response = new RecipePageResponse();
    response.setContent(recipes.getContent());
    response.setTotalElements(recipes.getTotalElements());
    response.setTotalPages(recipes.getTotalPages());
    response.setNumber(recipes.getNumber());
    response.setSize(recipes.getSize());
    response.setFirst(recipes.isFirst());
    response.setLast(recipes.isLast());
    response.setEmpty(recipes.isEmpty());
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Get recipes by product",
      description = "Retrieve all recipes associated with a specific product with pagination")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved recipes by product",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RecipePageResponse.class)))
      })
  @GetMapping("/product/{productId}")
  public ResponseEntity<RecipePageResponse> getRecipesByProduct(
      @Parameter(description = "Product ID") @PathVariable Long productId,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<RecipeDto> recipes = recipeService.findByProduct(productId, pageable);
    RecipePageResponse response = new RecipePageResponse();
    response.setContent(recipes.getContent());
    response.setTotalElements(recipes.getTotalElements());
    response.setTotalPages(recipes.getTotalPages());
    response.setNumber(recipes.getNumber());
    response.setSize(recipes.getSize());
    response.setFirst(recipes.isFirst());
    response.setLast(recipes.isLast());
    response.setEmpty(recipes.isEmpty());
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Get recipe by ID",
      description = "Retrieve a specific recipe by its unique identifier")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Recipe found"),
        @ApiResponse(responseCode = "404", description = "Recipe not found")
      })
  @GetMapping("/{id}")
  public ResponseEntity<RecipeDto> getRecipeById(
      @Parameter(description = "Recipe ID", example = "1") @PathVariable Long id) {
    try {
      RecipeDto dto = recipeService.getById(id);
      return ResponseEntity.ok(dto);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /** Get recipe by recipe code */
  @Operation(
      summary = "Get recipe by recipe code",
      description = "Retrieve a specific recipe using its unique recipe code")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Recipe found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RecipeDto.class))),
        @ApiResponse(responseCode = "404", description = "Recipe not found")
      })
  @GetMapping("/code/{recipeCode}")
  public ResponseEntity<RecipeDto> getRecipeByCode(@PathVariable String recipeCode) {
    RecipeDto dto = recipeService.getByRecipeCode(recipeCode);
    if (dto == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(dto);
  }

  @Operation(
      summary = "Create new recipe",
      description = "Create a new pharmaceutical recipe with ingredients and cost calculations")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Recipe created successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input or recipe code already exists",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  @PostMapping
  public ResponseEntity<?> createRecipe(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Recipe details including ingredients",
              required = true)
          @Valid
          @RequestBody
          RecipeDto dto) {
    try {
      RecipeDto created = recipeService.create(dto);
      return ResponseEntity.status(HttpStatus.CREATED).body(created);
    } catch (RuntimeException e) {
      ErrorResponse error = new ErrorResponse();
      error.setError(e.getMessage());
      return ResponseEntity.badRequest().body(error);
    }
  }

  @Operation(
      summary = "Update existing recipe",
      description = "Update recipe details, ingredients, and recalculate costs")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Recipe updated successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Recipe not found")
      })
  @PutMapping("/{id}")
  public ResponseEntity<?> updateRecipe(
      @Parameter(description = "Recipe ID") @PathVariable Long id,
      @Valid @RequestBody RecipeDto dto) {
    try {
      RecipeDto updated = recipeService.update(id, dto);
      return ResponseEntity.ok(updated);
    } catch (RuntimeException e) {
      ErrorResponse error = new ErrorResponse();
      error.setError(e.getMessage());
      return ResponseEntity.badRequest().body(error);
    }
  }

  @Operation(
      summary = "Soft delete recipe",
      description = "Archive recipe (marks as inactive, does not delete from database)")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Recipe archived successfully"),
        @ApiResponse(responseCode = "404", description = "Recipe not found")
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRecipe(
      @Parameter(description = "Recipe ID") @PathVariable Long id) {
    try {
      recipeService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /** Hard delete recipe */
  @DeleteMapping("/{id}/hard")
  public ResponseEntity<Void> hardDelete(@PathVariable Long id) {
    recipeService.hardDelete(id);
    return ResponseEntity.noContent().build();
  }

  /** Copy recipe from existing */
  @Operation(
      summary = "Copy existing recipe",
      description = "Copy an existing recipe with a new code and optional name")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Recipe copied successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  @PostMapping("/{id}/copy")
  public ResponseEntity<?> copyRecipe(
      @PathVariable Long id,
      @RequestParam String newRecipeCode,
      @RequestParam(required = false) String newName) {
    try {
      RecipeDto copied = recipeService.copyRecipe(id, newRecipeCode, newName);
      return ResponseEntity.status(HttpStatus.CREATED).body(copied);
    } catch (RuntimeException e) {
      ErrorResponse error = new ErrorResponse();
      error.setError(e.getMessage());
      return ResponseEntity.badRequest().body(error);
    }
  }

  /** Get recipe statistics */
  @Operation(
      summary = "Get recipe statistics",
      description = "Retrieve statistics about recipes including counts by category")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved recipe statistics",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RecipeStatsResponse.class)))
      })
  @GetMapping("/stats")
  public ResponseEntity<RecipeStatsResponse> getRecipeStats() {
    List<RecipeDto> allRecipes = recipeService.listAll();
    List<RecipeDto> activeRecipes = recipeService.listAllActive();

    RecipeStatsResponse statsResponse = new RecipeStatsResponse();
    statsResponse.setTotalRecipes(allRecipes.size());
    statsResponse.setActiveRecipes(activeRecipes.size());
    statsResponse.setInactiveRecipes(allRecipes.size() - activeRecipes.size());

    // Count by category
    Map<String, Long> categoryCount = new HashMap<>();
    allRecipes.forEach(
        recipe -> {
          String category = recipe.getCategory() != null ? recipe.getCategory() : "Uncategorized";
          categoryCount.put(category, categoryCount.getOrDefault(category, 0L) + 1);
        });
    statsResponse.setCategoryBreakdown(categoryCount);

    return ResponseEntity.ok(statsResponse);
  }
}
