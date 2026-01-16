/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
  Optional<Customer> findByCustomerCode(String customerCode);

  Optional<Customer> findByEmail(String email);

  List<Customer> findByStatus(Customer.CustomerStatus status);

  List<Customer> findByType(Customer.CustomerType type);

  List<Customer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
      String firstName, String lastName);

  boolean existsByCustomerCode(String customerCode);

  boolean existsByEmail(String email);
}
