/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.Ingredient;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
  List<Ingredient> findByCurrentStockLessThan(BigDecimal threshold);

  List<Ingredient> findByCurrentStockGreaterThan(BigDecimal threshold);
}
