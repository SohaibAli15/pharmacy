package com.pharmacy.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "recipes", indexes = {
    @Index(name = "idx_recipe_code", columnList = "recipeCode"),
    @Index(name = "idx_recipe_category", columnList = "category"),
    @Index(name = "idx_recipe_product", columnList = "productId")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String recipeCode; // e.g., (0964), (1248)

    @Column(nullable = false, length = 255)
    private String name; // e.g., "Unit Cotton Aglop 400 mg caps"

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 100)
    private String category; // e.g., "Packaging Material", "F.P"

    @Column(length = 100)
    private String subCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    private Medicine product; // Link to finished product

    // Production output specifications
    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal outputQuantity; // e.g., 1,237.500

    @Column(nullable = false, length = 50)
    private String outputUnit; // e.g., "Pc(s)", "KG", "Liters"

    // Cost breakdown
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalIngredientCost; // Sum of ingredient costs

    @Column(precision = 15, scale = 2)
    private BigDecimal fixedProductionCost; // Fixed cost per batch

    @Column(precision = 15, scale = 2)
    private BigDecimal variableProductionCost; // Variable cost based on quantity

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalCost; // Total recipe cost

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal unitPrice; // Rs 2.66, Rs 5.23

    @Column(precision = 5, scale = 2)
    private BigDecimal wastagePercent; // Expected wastage % (e.g., 3.00%)

    // Recipe instructions (rich text HTML content)
    @Column(columnDefinition = "TEXT")
    private String instructions;

    // Ingredients relationship
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<RecipeIngredient> ingredients;

    // Recipe status
    @Column(nullable = false, length = 20)
    private String status; // DRAFT, ACTIVE, INACTIVE, ARCHIVED

    @Column(nullable = false)
    private Boolean isActive = true;

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
