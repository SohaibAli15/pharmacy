package com.pharmacy.repository;

import com.pharmacy.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByCurrentStockLessThan(BigDecimal threshold);
    List<Ingredient> findByCurrentStockGreaterThan(BigDecimal threshold);
}
