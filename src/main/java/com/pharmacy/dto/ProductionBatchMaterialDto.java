package com.pharmacy.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductionBatchMaterialDto {
    private Long id;

    @NotNull(message = "Ingredient ID is required")
    private Long ingredientId;
    private String ingredientName;
    private String ingredientCode;

    @NotNull(message = "Quantity required is required")
    private BigDecimal quantityRequired;

    @NotNull(message = "Quantity used is required")
    @DecimalMin(value = "0.0", message = "Quantity used cannot be negative")
    private BigDecimal quantityUsed;

    @NotBlank(message = "Unit is required")
    private String unit;

    private BigDecimal variance;
    private BigDecimal variancePercent;
    private BigDecimal costPerUnit;
    private BigDecimal totalCost;
    private String lotNumber;
    private String notes;
}

