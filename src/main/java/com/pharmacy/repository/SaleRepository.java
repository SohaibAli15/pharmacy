/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.Customer;
import com.pharmacy.entity.Sale;
import com.pharmacy.entity.Store;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
  Optional<Sale> findByInvoiceNumber(String invoiceNumber);

  Page<Sale> findByStore(Store store, Pageable pageable);

  Page<Sale> findByCustomer(Customer customer, Pageable pageable);

  List<Sale> findBySaleDateBetween(LocalDateTime start, LocalDateTime end);

  Page<Sale> findByStatus(Sale.SaleStatus status, Pageable pageable);
}
