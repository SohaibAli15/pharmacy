/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.Customer;
import com.pharmacy.entity.CustomerStatusEntity;
import com.pharmacy.entity.CustomerTypeEntity;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
  Optional<Customer> findByCustomerCode(String customerCode);

  Optional<Customer> findByEmail(String email);

  Page<Customer> findByStatus(CustomerStatusEntity status, Pageable pageable);

  Page<Customer> findByType(CustomerTypeEntity type, Pageable pageable);

  Page<Customer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
      String firstName, String lastName, Pageable pageable);

  boolean existsByCustomerCode(String customerCode);

  boolean existsByEmail(String email);
}
