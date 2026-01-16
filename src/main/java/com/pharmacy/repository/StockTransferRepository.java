/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.StockTransfer;
import com.pharmacy.entity.Store;

@Repository
public interface StockTransferRepository extends JpaRepository<StockTransfer, Long> {
  Optional<StockTransfer> findByTransferNumber(String transferNumber);

  List<StockTransfer> findByFromStore(Store fromStore);

  List<StockTransfer> findByToStore(Store toStore);

  List<StockTransfer> findByStatus(StockTransfer.TransferStatus status);

  List<StockTransfer> findByTransferDateBetween(LocalDate startDate, LocalDate endDate);

  List<StockTransfer> findByFromStoreOrToStore(Store fromStore, Store toStore);

  boolean existsByTransferNumber(String transferNumber);
}
