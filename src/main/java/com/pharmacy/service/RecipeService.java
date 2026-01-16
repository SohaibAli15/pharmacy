/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.RecipeDto;
import com.pharmacy.dto.RecipeIngredientDto;
import com.pharmacy.entity.Medicine;
import com.pharmacy.entity.Recipe;
import com.pharmacy.entity.RecipeIngredient;
import com.pharmacy.repository.IngredientRepository;
import com.pharmacy.repository.MedicineRepository;
import com.pharmacy.repository.RecipeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeService {

  private final RecipeRepository recipeRepository;
  private final IngredientRepository ingredientRepository;
  private final MedicineRepository medicineRepository;

  public RecipeDto toDto(Recipe r) {
    RecipeDto dto = new RecipeDto();
    dto.setId(r.getId());
    dto.setRecipeCode(r.getRecipeCode());
    dto.setName(r.getName());
    dto.setDescription(r.getDescription());
    dto.setCategory(r.getCategory());
    dto.setSubCategory(r.getSubCategory());

    // Product information
    if (r.getProduct() != null) {
      dto.setProductId(r.getProduct().getId());
      dto.setProductName(r.getProduct().getName());
    }

    // Output specifications
    dto.setOutputQuantity(r.getOutputQuantity());
    dto.setOutputUnit(r.getOutputUnit());

    // Cost breakdown
    dto.setTotalIngredientCost(r.getTotalIngredientCost());
    dto.setFixedProductionCost(r.getFixedProductionCost());
    dto.setVariableProductionCost(r.getVariableProductionCost());
    dto.setTotalCost(r.getTotalCost());
    dto.setUnitPrice(r.getUnitPrice());
    dto.setWastagePercent(r.getWastagePercent());

    // Instructions
    dto.setInstructions(r.getInstructions());

    // Ingredients
    if (r.getIngredients() != null) {
      dto.setIngredients(
          r.getIngredients().stream()
              .map(this::toRecipeIngredientDto)
              .collect(Collectors.toList()));
    }

    // Status
    dto.setStatus(r.getStatus());
    dto.setIsActive(r.getIsActive());

    // Audit fields
    dto.setCreatedAt(r.getCreatedAt());
    dto.setUpdatedAt(r.getUpdatedAt());
    dto.setCreatedBy(r.getCreatedBy());
    dto.setUpdatedBy(r.getUpdatedBy());

    return dto;
  }

  public RecipeIngredientDto toRecipeIngredientDto(RecipeIngredient ri) {
    RecipeIngredientDto dto = new RecipeIngredientDto();
    dto.setId(ri.getId());
    dto.setIngredientId(ri.getIngredient().getId());
    dto.setIngredientName(ri.getIngredient().getName());
    dto.setQuantityRequired(ri.getQuantityRequired());
    dto.setUnit(ri.getUnit());
    dto.setWastagePercent(ri.getWastagePercent());
    dto.setFinalQuantity(ri.getFinalQuantity());
    dto.setCostPerUnit(ri.getCostPerUnit());
    dto.setTotalCost(ri.getTotalCost());
    dto.setSortOrder(ri.getSortOrder());
    return dto;
  }

  public Recipe fromDto(RecipeDto dto) {
    Recipe r = new Recipe();
    r.setId(dto.getId());
    r.setRecipeCode(dto.getRecipeCode());
    r.setName(dto.getName());
    r.setDescription(dto.getDescription());
    r.setCategory(dto.getCategory());
    r.setSubCategory(dto.getSubCategory());

    // Set product if provided
    if (dto.getProductId() != null) {
      Medicine product = medicineRepository.findById(dto.getProductId()).orElse(null);
      r.setProduct(product);
    }

    // Output specifications
    r.setOutputQuantity(dto.getOutputQuantity());
    r.setOutputUnit(dto.getOutputUnit());

    // Cost fields with defaults
    r.setFixedProductionCost(
        dto.getFixedProductionCost() != null ? dto.getFixedProductionCost() : BigDecimal.ZERO);
    r.setVariableProductionCost(
        dto.getVariableProductionCost() != null
            ? dto.getVariableProductionCost()
            : BigDecimal.ZERO);
    r.setTotalIngredientCost(
        dto.getTotalIngredientCost() != null ? dto.getTotalIngredientCost() : BigDecimal.ZERO);
    r.setTotalCost(dto.getTotalCost() != null ? dto.getTotalCost() : BigDecimal.ZERO);
    r.setUnitPrice(dto.getUnitPrice() != null ? dto.getUnitPrice() : BigDecimal.ZERO);
    r.setWastagePercent(
        dto.getWastagePercent() != null ? dto.getWastagePercent() : BigDecimal.ZERO);

    // Instructions
    r.setInstructions(dto.getInstructions());

    // Status fields
    r.setStatus(dto.getStatus() != null ? dto.getStatus() : "DRAFT");
    r.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

    // Audit fields
    r.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now());
    r.setUpdatedAt(LocalDateTime.now());
    r.setCreatedBy(dto.getCreatedBy());
    r.setUpdatedBy(dto.getUpdatedBy());

    // Ingredients will be set separately
    return r;
  }

  public RecipeDto create(RecipeDto dto) {
    // Validate unique recipe code
    if (recipeRepository.findByRecipeCode(dto.getRecipeCode()).isPresent()) {
      throw new RuntimeException("Recipe code already exists: " + dto.getRecipeCode());
    }

    Recipe r = fromDto(dto);

    // Save recipe first to get ID
    Recipe saved = recipeRepository.save(r);

    // Process ingredients
    if (dto.getIngredients() != null && !dto.getIngredients().isEmpty()) {
      final Recipe finalSaved = saved;
      List<RecipeIngredient> ingredients =
          dto.getIngredients().stream()
              .map(
                  riDto -> {
                    RecipeIngredient ri = new RecipeIngredient();
                    ri.setRecipe(finalSaved);

                    var ingredient =
                        ingredientRepository
                            .findById(riDto.getIngredientId())
                            .orElseThrow(
                                () ->
                                    new RuntimeException(
                                        "Ingredient not found: " + riDto.getIngredientId()));
                    ri.setIngredient(ingredient);

                    ri.setQuantityRequired(riDto.getQuantityRequired());
                    ri.setUnit(riDto.getUnit() != null ? riDto.getUnit() : ingredient.getUnit());
                    ri.setWastagePercent(
                        riDto.getWastagePercent() != null
                            ? riDto.getWastagePercent()
                            : BigDecimal.ZERO);
                    ri.setCostPerUnit(ingredient.getCostPerUnit());
                    ri.setSortOrder(riDto.getSortOrder());

                    return ri;
                  })
              .collect(Collectors.toList());

      saved.setIngredients(ingredients);

      // Calculate costs
      calculateRecipeCosts(saved);

      saved = recipeRepository.save(saved);
    }

    return toDto(saved);
  }

  public RecipeDto update(Long id, RecipeDto dto) {
    Recipe existing =
        recipeRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Recipe not found: " + id));

    // Update basic fields
    existing.setName(dto.getName());
    existing.setDescription(dto.getDescription());
    existing.setCategory(dto.getCategory());
    existing.setSubCategory(dto.getSubCategory());

    if (dto.getProductId() != null) {
      Medicine product = medicineRepository.findById(dto.getProductId()).orElse(null);
      existing.setProduct(product);
    }

    existing.setOutputQuantity(dto.getOutputQuantity());
    existing.setOutputUnit(dto.getOutputUnit());
    existing.setFixedProductionCost(dto.getFixedProductionCost());
    existing.setVariableProductionCost(dto.getVariableProductionCost());
    existing.setWastagePercent(dto.getWastagePercent());
    existing.setInstructions(dto.getInstructions());
    existing.setStatus(dto.getStatus());
    existing.setIsActive(dto.getIsActive());
    existing.setUpdatedBy(dto.getUpdatedBy());

    // Update ingredients if provided
    if (dto.getIngredients() != null) {
      existing.getIngredients().clear();

      final Recipe finalExisting = existing;
      List<RecipeIngredient> ingredients =
          dto.getIngredients().stream()
              .map(
                  riDto -> {
                    RecipeIngredient ri = new RecipeIngredient();
                    ri.setRecipe(finalExisting);

                    var ingredient =
                        ingredientRepository
                            .findById(riDto.getIngredientId())
                            .orElseThrow(
                                () ->
                                    new RuntimeException(
                                        "Ingredient not found: " + riDto.getIngredientId()));
                    ri.setIngredient(ingredient);

                    ri.setQuantityRequired(riDto.getQuantityRequired());
                    ri.setUnit(riDto.getUnit() != null ? riDto.getUnit() : ingredient.getUnit());
                    ri.setWastagePercent(
                        riDto.getWastagePercent() != null
                            ? riDto.getWastagePercent()
                            : BigDecimal.ZERO);
                    ri.setCostPerUnit(ingredient.getCostPerUnit());
                    ri.setSortOrder(riDto.getSortOrder());

                    return ri;
                  })
              .collect(Collectors.toList());

      existing.setIngredients(ingredients);

      // Recalculate costs
      calculateRecipeCosts(existing);
    }

    Recipe saved = recipeRepository.save(existing);
    return toDto(saved);
  }

  public void delete(Long id) {
    Recipe recipe =
        recipeRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Recipe not found: " + id));

    // Soft delete - mark as inactive
    recipe.setIsActive(false);
    recipe.setStatus("ARCHIVED");
    recipeRepository.save(recipe);
  }

  public void hardDelete(Long id) {
    recipeRepository.deleteById(id);
  }

  public RecipeDto getById(Long id) {
    return recipeRepository
        .findById(id)
        .map(this::toDto)
        .orElseThrow(() -> new RuntimeException("Recipe not found: " + id));
  }

  public RecipeDto getByRecipeCode(String recipeCode) {
    return recipeRepository.findByRecipeCode(recipeCode).map(this::toDto).orElse(null);
  }

  public List<RecipeDto> searchByName(String name) {
    return recipeRepository.findByNameContainingIgnoreCase(name).stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public List<RecipeDto> listAll() {
    return recipeRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
  }

  public List<RecipeDto> listAllActive() {
    return recipeRepository.findAllActiveRecipes().stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public List<RecipeDto> searchRecipes(
      String category, String subCategory, String status, Long productId, String searchTerm) {
    return recipeRepository
        .searchRecipes(category, subCategory, status, productId, searchTerm)
        .stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public List<RecipeDto> findByCategory(String category) {
    return recipeRepository.findByCategory(category).stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public List<RecipeDto> findByProduct(Long productId) {
    return recipeRepository.findByProductId(productId).stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public RecipeDto copyRecipe(Long recipeId, String newRecipeCode, String newName) {
    Recipe original =
        recipeRepository
            .findById(recipeId)
            .orElseThrow(() -> new RuntimeException("Recipe not found: " + recipeId));

    Recipe copy = new Recipe();
    copy.setRecipeCode(newRecipeCode);
    copy.setName(newName != null ? newName : original.getName() + " (Copy)");
    copy.setDescription(original.getDescription());
    copy.setCategory(original.getCategory());
    copy.setSubCategory(original.getSubCategory());
    copy.setProduct(original.getProduct());
    copy.setOutputQuantity(original.getOutputQuantity());
    copy.setOutputUnit(original.getOutputUnit());
    copy.setFixedProductionCost(original.getFixedProductionCost());
    copy.setVariableProductionCost(original.getVariableProductionCost());
    copy.setWastagePercent(original.getWastagePercent());
    copy.setInstructions(original.getInstructions());
    copy.setStatus("DRAFT");
    copy.setIsActive(true);

    Recipe savedCopy = recipeRepository.save(copy);

    // Copy ingredients
    if (original.getIngredients() != null) {
      final Recipe finalSavedCopy = savedCopy;
      List<RecipeIngredient> copiedIngredients =
          original.getIngredients().stream()
              .map(
                  ri -> {
                    RecipeIngredient newRi = new RecipeIngredient();
                    newRi.setRecipe(finalSavedCopy);
                    newRi.setIngredient(ri.getIngredient());
                    newRi.setQuantityRequired(ri.getQuantityRequired());
                    newRi.setUnit(ri.getUnit());
                    newRi.setWastagePercent(ri.getWastagePercent());
                    newRi.setCostPerUnit(ri.getCostPerUnit());
                    newRi.setSortOrder(ri.getSortOrder());
                    return newRi;
                  })
              .collect(Collectors.toList());

      savedCopy.setIngredients(copiedIngredients);
      calculateRecipeCosts(savedCopy);
      savedCopy = recipeRepository.save(savedCopy);
    }

    return toDto(savedCopy);
  }

  /**
   * Calculate all costs for a recipe including ingredient costs, production costs, and unit price
   */
  private void calculateRecipeCosts(Recipe recipe) {
    // Calculate costs for each ingredient first (before @PrePersist)
    if (recipe.getIngredients() != null) {
      for (RecipeIngredient ri : recipe.getIngredients()) {
        // Calculate final quantity with wastage
        if (ri.getWastagePercent() != null
            && ri.getWastagePercent().compareTo(BigDecimal.ZERO) > 0) {
          BigDecimal wastageMultiplier =
              BigDecimal.ONE.add(
                  ri.getWastagePercent().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
          ri.setFinalQuantity(ri.getQuantityRequired().multiply(wastageMultiplier));
        } else {
          ri.setFinalQuantity(ri.getQuantityRequired());
        }

        // Calculate total cost
        if (ri.getCostPerUnit() != null && ri.getFinalQuantity() != null) {
          ri.setTotalCost(ri.getFinalQuantity().multiply(ri.getCostPerUnit()));
        } else {
          ri.setTotalCost(BigDecimal.ZERO);
        }
      }
    }

    // Calculate total ingredient cost (with wastage)
    BigDecimal totalIngredientCost =
        recipe.getIngredients() != null
            ? recipe.getIngredients().stream()
                .map(ri -> ri.getTotalCost() != null ? ri.getTotalCost() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
            : BigDecimal.ZERO;

    recipe.setTotalIngredientCost(totalIngredientCost);

    // Calculate total cost
    BigDecimal fixedCost =
        recipe.getFixedProductionCost() != null ? recipe.getFixedProductionCost() : BigDecimal.ZERO;
    BigDecimal variableCost =
        recipe.getVariableProductionCost() != null
            ? recipe.getVariableProductionCost()
            : BigDecimal.ZERO;
    BigDecimal totalCost = totalIngredientCost.add(fixedCost).add(variableCost);

    recipe.setTotalCost(totalCost);

    // Calculate unit price
    if (recipe.getOutputQuantity() != null
        && recipe.getOutputQuantity().compareTo(BigDecimal.ZERO) > 0) {
      BigDecimal unitPrice = totalCost.divide(recipe.getOutputQuantity(), 4, RoundingMode.HALF_UP);
      recipe.setUnitPrice(unitPrice);
    } else {
      recipe.setUnitPrice(BigDecimal.ZERO);
    }
  }
}
