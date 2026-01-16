/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.Customer;
import com.pharmacy.entity.Sale;
import com.pharmacy.entity.Store;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
  Optional<Sale> findByInvoiceNumber(String invoiceNumber);

  List<Sale> findByStore(Store store);

  List<Sale> findByCustomer(Customer customer);

  List<Sale> findBySaleDateBetween(LocalDateTime start, LocalDateTime end);

  List<Sale> findByStatus(Sale.SaleStatus status);
}
