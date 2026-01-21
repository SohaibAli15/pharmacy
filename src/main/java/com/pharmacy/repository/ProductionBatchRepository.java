/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.ProductionBatch;

@Repository
public interface ProductionBatchRepository
    extends JpaRepository<ProductionBatch, Long>, JpaSpecificationExecutor<ProductionBatch> {

  Optional<ProductionBatch> findByReferenceNumber(String referenceNumber);

  List<ProductionBatch> findByRecipeId(Long recipeId);

  // Pageable overload for recipe
  Page<ProductionBatch> findByRecipeId(Long recipeId, Pageable pageable);

  List<ProductionBatch> findByStatus(String status);

  // Pageable overload for status
  Page<ProductionBatch> findByStatus(String status, Pageable pageable);

  // Pageable overload for business location
  Page<ProductionBatch> findByBusinessLocation(String businessLocation, Pageable pageable);

  List<ProductionBatch> findByIsFinalized(Boolean isFinalized);

  List<ProductionBatch> findByProductionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

  @Query(
      "SELECT pb FROM ProductionBatch pb WHERE "
          + "(:businessLocation IS NULL OR pb.businessLocation = :businessLocation) AND "
          + "(:status IS NULL OR pb.status = :status) AND "
          + "(:isFinalized IS NULL OR pb.isFinalized = :isFinalized) AND "
          + "(:recipeId IS NULL OR pb.recipe.id = :recipeId) AND "
          + "(:productId IS NULL OR pb.product.id = :productId) AND "
          + "(:startDate IS NULL OR pb.productionDate >= :startDate) AND "
          + "(:endDate IS NULL OR pb.productionDate <= :endDate) "
          + "ORDER BY pb.productionDate DESC")
  List<ProductionBatch> searchProductionBatches(
      @Param("businessLocation") String businessLocation,
      @Param("status") String status,
      @Param("isFinalized") Boolean isFinalized,
      @Param("recipeId") Long recipeId,
      @Param("productId") Long productId,
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate);

  // Pageable overload returning Page
  @Query(
      "SELECT pb FROM ProductionBatch pb WHERE "
          + "(:businessLocation IS NULL OR pb.businessLocation = :businessLocation) AND "
          + "(:status IS NULL OR pb.status = :status) AND "
          + "(:isFinalized IS NULL OR pb.isFinalized = :isFinalized) AND "
          + "(:recipeId IS NULL OR pb.recipe.id = :recipeId) AND "
          + "(:productId IS NULL OR pb.product.id = :productId) AND "
          + "(:startDate IS NULL OR pb.productionDate >= :startDate) AND "
          + "(:endDate IS NULL OR pb.productionDate <= :endDate) "
          + "ORDER BY pb.productionDate DESC")
  Page<ProductionBatch> searchProductionBatches(
      @Param("businessLocation") String businessLocation,
      @Param("status") String status,
      @Param("isFinalized") Boolean isFinalized,
      @Param("recipeId") Long recipeId,
      @Param("productId") Long productId,
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate,
      Pageable pageable);

  @Query("SELECT DISTINCT pb.businessLocation FROM ProductionBatch pb ORDER BY pb.businessLocation")
  List<String> findAllBusinessLocations();

  @Query(
      "SELECT pb FROM ProductionBatch pb WHERE pb.productionDate >= :startDate ORDER BY pb.productionDate DESC")
  List<ProductionBatch> findRecentBatches(@Param("startDate") LocalDateTime startDate);
}
