/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pharmacy.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
  Employee findByEmployeeId(String employeeId);
}
