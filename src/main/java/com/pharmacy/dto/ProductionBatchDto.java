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
public class ProductionBatchDto {
  private Long id;

  @NotNull(message = "Recipe ID is required") private Long recipeId;

  private String recipeName;
  private String recipeCode;

  private String referenceNumber; // Auto-generated if not provided

  @NotNull(message = "Production date is required") private LocalDateTime productionDate;

  @NotBlank(message = "Business location is required")
  private String businessLocation;

  private Long productId;
  private String productName;

  @NotNull(message = "Quantity produced is required") @DecimalMin(value = "0.001", message = "Quantity must be greater than 0")
  private BigDecimal quantityProduced;

  private BigDecimal expectedQuantity;
  private BigDecimal wastedQuantity;

  @NotBlank(message = "Unit is required")
  private String unit;

  private BigDecimal totalCost;
  private BigDecimal productionCost;
  private BigDecimal ingredientCost;

  private String status;
  private Boolean isFinalized;
  private LocalDateTime finalizedAt;
  private String finalizedBy;

  private String lotNumber;
  private String attachedDocumentPath;
  private String notes;

  private List<ProductionBatchMaterialDto> materialsConsumed;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String createdBy;
  private String updatedBy;

  private Long workOrderId;
}
