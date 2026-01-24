/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.pharmacy.entity.Employee;
import com.pharmacy.repository.EmployeeRepository;
import com.pharmacy.service.PayrollService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {
  private final EmployeeRepository employeeRepository;

  @Override
  public double getCurrentMonthPayrollTotal() {
    LocalDate now = LocalDate.now();
    return employeeRepository.findAll().stream()
        .filter(e -> "Active".equalsIgnoreCase(e.getStatus()))
        .filter(
            e ->
                e.getJoiningDate() != null
                    && !e.getJoiningDate()
                        .isAfter(now.withDayOfMonth(1).plusMonths(1).minusDays(1)))
        .map(Employee::getSalary)
        .filter(s -> s != null)
        .mapToDouble(BigDecimal::doubleValue)
        .sum();
  }
}
