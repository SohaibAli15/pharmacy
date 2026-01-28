/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.WorkOrder;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
  @Query("SELECT w FROM WorkOrder w LEFT JOIN FETCH w.productionBatches WHERE w.id = :id")
  Optional<WorkOrder> findByIdWithBatches(@Param("id") Long id);

  @Query("SELECT DISTINCT w FROM WorkOrder w LEFT JOIN FETCH w.productionBatches")
  List<WorkOrder> findAllWithBatches();
}
