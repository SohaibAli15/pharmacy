/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.Medicine;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
  Page<Medicine> findByNameContainingIgnoreCase(String name, Pageable pageable);

  Page<Medicine> findByCategory(String category, Pageable pageable);
}
