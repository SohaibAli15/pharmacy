/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pharmacy.entity.GeneralLedgerEntry;

public interface GeneralLedgerEntryRepository extends JpaRepository<GeneralLedgerEntry, Long> {
  List<GeneralLedgerEntry> findByCompany(String company);
}
