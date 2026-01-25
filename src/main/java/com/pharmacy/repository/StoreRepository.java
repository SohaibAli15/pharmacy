/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.Store;
import com.pharmacy.entity.StoreStatusEntity;
import com.pharmacy.entity.StoreTypeEntity;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
  Optional<Store> findByCode(String code);

  Page<Store> findByStatus(StoreStatusEntity status, Pageable pageable);

  Page<Store> findByType(StoreTypeEntity type, Pageable pageable);

  List<Store> findByNameContainingIgnoreCase(String name);

  boolean existsByCode(String code);
}
