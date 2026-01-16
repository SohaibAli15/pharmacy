/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.PurchaseOrder;
import com.pharmacy.entity.Store;
import com.pharmacy.entity.Supplier;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
  Optional<PurchaseOrder> findByOrderNumber(String orderNumber);

  List<PurchaseOrder> findBySupplier(Supplier supplier);

  List<PurchaseOrder> findByStore(Store store);

  List<PurchaseOrder> findByStatus(PurchaseOrder.OrderStatus status);

  List<PurchaseOrder> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);

  List<PurchaseOrder> findBySupplierAndStatus(Supplier supplier, PurchaseOrder.OrderStatus status);

  boolean existsByOrderNumber(String orderNumber);
}
