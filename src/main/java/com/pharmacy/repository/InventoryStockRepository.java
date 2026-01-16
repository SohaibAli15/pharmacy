/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.InventoryStock;
import com.pharmacy.entity.Medicine;
import com.pharmacy.entity.Store;

@Repository
public interface InventoryStockRepository extends JpaRepository<InventoryStock, Long> {
  List<InventoryStock> findByStore(Store store);

  List<InventoryStock> findByMedicine(Medicine medicine);

  List<InventoryStock> findByStoreAndMedicine(Store store, Medicine medicine);

  Optional<InventoryStock> findByStoreAndMedicineAndBatchNumber(
      Store store, Medicine medicine, String batchNumber);

  @Query("SELECT i FROM InventoryStock i WHERE i.store = :store AND i.quantity <= i.reorderLevel")
  List<InventoryStock> findLowStockItems(Store store);

  @Query("SELECT i FROM InventoryStock i WHERE i.store = :store AND i.expiryDate <= :date")
  List<InventoryStock> findExpiringStock(Store store, LocalDate date);
}
