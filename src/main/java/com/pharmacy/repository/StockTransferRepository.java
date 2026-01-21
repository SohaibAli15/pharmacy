/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.StockTransfer;
import com.pharmacy.entity.Store;

@Repository
public interface StockTransferRepository extends JpaRepository<StockTransfer, Long> {
  Optional<StockTransfer> findByTransferNumber(String transferNumber);

  Page<StockTransfer> findByFromStore(Store fromStore, Pageable pageable);

  Page<StockTransfer> findByToStore(Store toStore, Pageable pageable);

  Page<StockTransfer> findByStatus(StockTransfer.TransferStatus status, Pageable pageable);

  List<StockTransfer> findByTransferDateBetween(LocalDate startDate, LocalDate endDate);

  Page<StockTransfer> findByFromStoreOrToStore(Store fromStore, Store toStore, Pageable pageable);

  boolean existsByTransferNumber(String transferNumber);
}
