/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;

import com.pharmacy.dto.PayrollDto;

public interface PayrollManagementService {
  PayrollDto create(PayrollDto dto);

  PayrollDto update(Long id, PayrollDto dto);

  void delete(Long id);

  PayrollDto getById(Long id);

  List<PayrollDto> getAll();
}
