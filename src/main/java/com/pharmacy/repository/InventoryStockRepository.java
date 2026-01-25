/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.InventoryStock;
import com.pharmacy.entity.Product;
import com.pharmacy.entity.Store;

@Repository
public interface InventoryStockRepository extends JpaRepository<InventoryStock, Long> {
  List<InventoryStock> findByStoreAndProduct(Store store, Product product);

  Optional<InventoryStock> findByStoreAndProductAndBatchNumber(
      Store store, Product product, String batchNumber);

  Page<InventoryStock> findByStore(Store store, Pageable pageable);

  Page<InventoryStock> findByProduct(Product product, Pageable pageable);

  @Query("SELECT i FROM InventoryStock i WHERE i.store = :store AND i.quantity <= i.reorderLevel")
  Page<InventoryStock> findLowStockItems(Store store, Pageable pageable);

  @Query("SELECT i FROM InventoryStock i WHERE i.store = :store AND i.expiryDate <= :date")
  Page<InventoryStock> findExpiringStock(Store store, LocalDate date, Pageable pageable);
}
