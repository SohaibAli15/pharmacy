/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.Prescription;
import com.pharmacy.entity.User;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

  Page<Prescription> findByCustomer(User customer, Pageable pageable);

  Page<Prescription> findByPharmacist(User pharmacist, Pageable pageable);

  Page<Prescription> findByStatus(String status, Pageable pageable);
}
