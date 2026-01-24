/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service.impl;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pharmacy.entity.Employee;
import com.pharmacy.repository.EmployeeRepository;
import com.pharmacy.service.DepartmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
  private final EmployeeRepository employeeRepository;

  @Override
  public int getActiveDepartmentCount() {
    Set<String> activeDepartments =
        employeeRepository.findAll().stream()
            .filter(e -> "Active".equalsIgnoreCase(e.getStatus()))
            .map(Employee::getDepartment)
            .filter(d -> d != null && !d.isBlank())
            .collect(Collectors.toSet());
    return activeDepartments.size();
  }
}
