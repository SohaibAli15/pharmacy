package com.pharmacy.repository;

import com.pharmacy.entity.ProductionBatchMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductionBatchMaterialRepository extends JpaRepository<ProductionBatchMaterial, Long> {

    List<ProductionBatchMaterial> findByProductionBatchId(Long productionBatchId);

    List<ProductionBatchMaterial> findByIngredientId(Long ingredientId);

    @Query("SELECT pbm FROM ProductionBatchMaterial pbm WHERE " +
           "pbm.productionBatch.id = :batchId AND pbm.ingredient.id = :ingredientId")
    ProductionBatchMaterial findByBatchAndIngredient(
        @Param("batchId") Long batchId,
        @Param("ingredientId") Long ingredientId
    );

    @Query("SELECT SUM(pbm.quantityUsed) FROM ProductionBatchMaterial pbm WHERE pbm.ingredient.id = :ingredientId")
    Double getTotalQuantityUsedForIngredient(@Param("ingredientId") Long ingredientId);
}

