/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.Supplier;
import com.pharmacy.entity.SupplierStatusEntity;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
  boolean existsByCode(String code);

  List<Supplier> findByNameContainingIgnoreCase(String name);

  List<Supplier> findByStatus(SupplierStatusEntity status);

  Optional<Supplier> findByCode(String code);
}
