/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pharmacy.entity.JournalVoucher;

public interface JournalVoucherRepository extends JpaRepository<JournalVoucher, Long> {
  List<JournalVoucher> findByCompany(String company);
}
