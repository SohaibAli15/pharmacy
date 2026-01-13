package com.pharmacy.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "recipe_ingredients", indexes = {
    @Index(name = "idx_recipe_ingredient_recipe", columnList = "recipe_id"),
    @Index(name = "idx_recipe_ingredient_ingredient", columnList = "ingredient_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal quantityRequired; // Required quantity per batch

    @Column(nullable = false, length = 50)
    private String unit; // Unit of measure (Packet, KG, Liters, etc.)

    @Column(precision = 5, scale = 2)
    private BigDecimal wastagePercent; // Wastage % for this ingredient (e.g., 0.00%)

    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal finalQuantity; // Final quantity including wastage

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal costPerUnit; // Cost snapshot at time of recipe creation

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalCost; // quantityRequired * costPerUnit

    @Column(precision = 10, scale = 0)
    private Integer sortOrder; // Display order in recipe

    @PrePersist
    @PreUpdate
    protected void calculateFinalQuantity() {
        if (wastagePercent != null && wastagePercent.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal wastageMultiplier = BigDecimal.ONE.add(wastagePercent.divide(new BigDecimal("100")));
            finalQuantity = quantityRequired.multiply(wastageMultiplier);
        } else {
            finalQuantity = quantityRequired;
        }

        if (costPerUnit != null) {
            totalCost = finalQuantity.multiply(costPerUnit);
        }
    }
}
