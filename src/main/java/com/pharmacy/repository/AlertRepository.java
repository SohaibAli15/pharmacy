/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.Alert;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
  Page<Alert> findByIsReadFalse(Pageable pageable);

  Page<Alert> findByIngredientId(Long ingredientId, Pageable pageable);
}
