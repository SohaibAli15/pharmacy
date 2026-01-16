/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.Prescription;
import com.pharmacy.entity.User;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
  List<Prescription> findByCustomer(User customer);

  List<Prescription> findByPharmacist(User pharmacist);

  List<Prescription> findByStatus(String status);
}
