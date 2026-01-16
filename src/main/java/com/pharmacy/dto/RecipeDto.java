/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {
  private Long id;

  @NotBlank(message = "Recipe code is required")
  private String recipeCode;

  @NotBlank(message = "Recipe name is required")
  private String name;

  private String description;

  @NotBlank(message = "Category is required")
  private String category;

  private String subCategory;

  private Long productId;
  private String productName;

  @NotNull(message = "Output quantity is required") @DecimalMin(value = "0.001", message = "Output quantity must be greater than 0")
  private BigDecimal outputQuantity;

  @NotBlank(message = "Output unit is required")
  private String outputUnit;

  private BigDecimal totalIngredientCost;
  private BigDecimal fixedProductionCost;
  private BigDecimal variableProductionCost;
  private BigDecimal totalCost;
  private BigDecimal unitPrice;
  private BigDecimal wastagePercent;

  private String instructions;

  @NotEmpty(message = "At least one ingredient is required")
  private List<RecipeIngredientDto> ingredients;

  private String status;
  private Boolean isActive;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String createdBy;
  private String updatedBy;
}
