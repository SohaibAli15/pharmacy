/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.Ingredient;
import com.pharmacy.entity.IngredientStock;
import com.pharmacy.entity.Store;

@Repository
public interface IngredientStockRepository extends JpaRepository<IngredientStock, Long> {
  List<IngredientStock> findByStore(Store store);

  List<IngredientStock> findByIngredient(Ingredient ingredient);

  List<IngredientStock> findByStoreAndIngredient(Store store, Ingredient ingredient);

  Optional<IngredientStock> findByStoreAndIngredientAndBatchNumber(
      Store store, Ingredient ingredient, String batchNumber);

  @Query(
      "SELECT SUM(i.quantity) FROM IngredientStock i WHERE i.store = :store AND i.ingredient = :ingredient")
  Double getTotalQuantityByStoreAndIngredient(Store store, Ingredient ingredient);
}
