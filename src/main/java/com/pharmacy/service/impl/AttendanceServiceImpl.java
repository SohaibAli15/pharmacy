/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pharmacy.entity.Employee;
import com.pharmacy.repository.EmployeeRepository;
import com.pharmacy.service.AttendanceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
  private final EmployeeRepository employeeRepository;

  @Override
  public double getCurrentMonthAverageAttendance() {
    // TODO: Replace with actual attendance calculation using AttendanceRepository
    List<Employee> employees = employeeRepository.findAll();
    if (employees.isEmpty()) return 0.0;
    // Placeholder: Assume 95% attendance for all active employees
    long activeCount =
        employees.stream().filter(e -> "Active".equalsIgnoreCase(e.getStatus())).count();
    return activeCount == 0 ? 0.0 : 95.0;
  }
}
