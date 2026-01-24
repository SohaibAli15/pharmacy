/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pharmacy.entity.AdjustmentNote;

public interface AdjustmentNoteRepository extends JpaRepository<AdjustmentNote, Long> {
  List<AdjustmentNote> findByCompany(String company);
}
