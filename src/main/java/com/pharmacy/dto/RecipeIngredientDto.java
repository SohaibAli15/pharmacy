/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientDto {
  private Long id;

  @NotNull(message = "Ingredient ID is required") private Long ingredientId;

  private String ingredientName;
  private String ingredientCode;

  @JsonProperty("quantity")
  @NotNull(message = "Quantity is required") @DecimalMin(value = "0.0001", message = "Quantity must be greater than 0")
  private BigDecimal quantityRequired;

  @NotBlank(message = "Unit is required")
  private String unit;

  private BigDecimal wastagePercent;
  private BigDecimal finalQuantity;
  private BigDecimal costPerUnit;
  private BigDecimal totalCost;
  private Integer sortOrder;
}
