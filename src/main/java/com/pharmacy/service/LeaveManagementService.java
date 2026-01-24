/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;

import com.pharmacy.dto.LeaveRequestDto;

public interface LeaveManagementService {
  LeaveRequestDto create(LeaveRequestDto dto);

  LeaveRequestDto update(Long id, LeaveRequestDto dto);

  void delete(Long id);

  LeaveRequestDto getById(Long id);

  List<LeaveRequestDto> getAll();
}
