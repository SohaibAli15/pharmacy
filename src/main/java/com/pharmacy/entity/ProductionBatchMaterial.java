package com.pharmacy.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

/**
 * Tracks actual material/ingredient consumption for each production batch
 * This provides detailed traceability for GMP compliance
 */
@Entity
@Table(name = "production_batch_materials", indexes = {
    @Index(name = "idx_pbm_batch", columnList = "production_batch_id"),
    @Index(name = "idx_pbm_ingredient", columnList = "ingredient_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductionBatchMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_batch_id", nullable = false)
    private ProductionBatch productionBatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal quantityRequired; // From recipe

    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal quantityUsed; // Actual quantity used

    @Column(nullable = false, length = 50)
    private String unit;

    @Column(precision = 15, scale = 4)
    private BigDecimal variance; // Difference (used - required)

    @Column(precision = 5, scale = 2)
    private BigDecimal variancePercent; // Variance as percentage

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal costPerUnit; // Cost at time of production

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalCost; // quantityUsed * costPerUnit

    @Column(length = 100)
    private String lotNumber; // Ingredient lot number for traceability

    @Column(columnDefinition = "TEXT")
    private String notes;

    @PrePersist
    @PreUpdate
    protected void calculateVariance() {
        if (quantityUsed != null && quantityRequired != null) {
            variance = quantityUsed.subtract(quantityRequired);

            if (quantityRequired.compareTo(BigDecimal.ZERO) > 0) {
                variancePercent = variance.divide(quantityRequired, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
            }
        }

        if (quantityUsed != null && costPerUnit != null) {
            totalCost = quantityUsed.multiply(costPerUnit);
        }
    }
}

