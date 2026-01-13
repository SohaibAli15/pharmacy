package com.pharmacy.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "production_batches", indexes = {
    @Index(name = "idx_production_batch_number", columnList = "referenceNumber"),
    @Index(name = "idx_production_recipe", columnList = "recipe_id"),
    @Index(name = "idx_production_location", columnList = "businessLocation"),
    @Index(name = "idx_production_date", columnList = "productionDate"),
    @Index(name = "idx_production_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductionBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Column(nullable = false, unique = true, length = 100)
    private String referenceNumber; // e.g., 02562026/4890

    @Column(nullable = false)
    private LocalDateTime productionDate;

    @Column(nullable = false, length = 150)
    private String businessLocation; // e.g., "Haram Science Center ISB", "Butt Brothers"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Medicine product; // Link to finished product

    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal quantityProduced; // Actual quantity produced

    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal expectedQuantity; // Expected quantity from recipe

    @Column(precision = 15, scale = 3)
    private BigDecimal wastedQuantity; // Wasted/lost quantity

    @Column(nullable = false, length = 50)
    private String unit; // Unit of measure

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalCost; // Total production cost

    @Column(precision = 15, scale = 2)
    private BigDecimal productionCost; // Fixed + variable production cost

    @Column(precision = 15, scale = 2)
    private BigDecimal ingredientCost; // Total ingredient cost

    @Column(nullable = false, length = 30)
    private String status; // DRAFT, IN_PROGRESS, COMPLETED, FINALIZED, CANCELLED

    @Column(nullable = false)
    private Boolean isFinalized = false;

    @Column
    private LocalDateTime finalizedAt;

    @Column(length = 100)
    private String finalizedBy;

    // Lot/Batch tracking
    @Column(length = 100)
    private String lotNumber;

    // Document attachment
    @Column(columnDefinition = "TEXT")
    private String attachedDocumentPath;

    @Column(columnDefinition = "TEXT")
    private String notes;

    // Material consumption tracking - separate entity
    @OneToMany(mappedBy = "productionBatch", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ProductionBatchMaterial> materialsConsumed;

    // Audit fields
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(length = 100)
    private String createdBy;

    @Column(length = 100)
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = "DRAFT";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
