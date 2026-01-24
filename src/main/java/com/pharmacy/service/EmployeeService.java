/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;

import com.pharmacy.dto.EmployeeDto;

public interface EmployeeService {
  EmployeeDto create(EmployeeDto dto);

  EmployeeDto update(Long id, EmployeeDto dto);

  void delete(Long id);

  EmployeeDto getById(Long id);

  EmployeeDto getByEmployeeId(String employeeId);

  List<EmployeeDto> getAll();
}
