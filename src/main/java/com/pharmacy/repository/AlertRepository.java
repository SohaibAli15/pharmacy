/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.Alert;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
  List<Alert> findByIsReadFalse();

  List<Alert> findByIngredientId(Long ingredientId);
}
