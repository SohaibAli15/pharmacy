/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.Recipe;

@Repository
public interface RecipeRepository
    extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {

  Optional<Recipe> findByRecipeCode(String recipeCode);

  List<Recipe> findByNameContainingIgnoreCase(String name);

  List<Recipe> findByCategory(String category);

  List<Recipe> findByCategoryAndSubCategory(String category, String subCategory);

  List<Recipe> findByProductId(Long productId);

  List<Recipe> findByStatus(String status);

  List<Recipe> findByIsActive(Boolean isActive);

  @Query("SELECT r FROM Recipe r WHERE r.isActive = true AND r.status = 'ACTIVE'")
  List<Recipe> findAllActiveRecipes();

  @Query(
      "SELECT r FROM Recipe r WHERE "
          + "(:category IS NULL OR r.category = :category) AND "
          + "(:subCategory IS NULL OR r.subCategory = :subCategory) AND "
          + "(:status IS NULL OR r.status = :status) AND "
          + "(:productId IS NULL OR r.product.id = :productId) AND "
          + "(:searchTerm IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(r.recipeCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
  List<Recipe> searchRecipes(
      @Param("category") String category,
      @Param("subCategory") String subCategory,
      @Param("status") String status,
      @Param("productId") Long productId,
      @Param("searchTerm") String searchTerm);

  @Query("SELECT r FROM Recipe r WHERE r.unitPrice BETWEEN :minPrice AND :maxPrice")
  List<Recipe> findByPriceRange(
      @Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
}
