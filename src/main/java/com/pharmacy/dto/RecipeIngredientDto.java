package com.pharmacy.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientDto {
    private Long id;

    @NotNull(message = "Ingredient ID is required")
    private Long ingredientId;

    private String ingredientName;
    private String ingredientCode;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.0001", message = "Quantity must be greater than 0")
    private BigDecimal quantityRequired;

    @NotBlank(message = "Unit is required")
    private String unit;

    private BigDecimal wastagePercent;
    private BigDecimal finalQuantity;
    private BigDecimal costPerUnit;
    private BigDecimal totalCost;
    private Integer sortOrder;
}
