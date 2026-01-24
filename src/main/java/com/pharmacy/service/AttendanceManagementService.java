/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;

import com.pharmacy.dto.AttendanceDto;

public interface AttendanceManagementService {
  AttendanceDto create(AttendanceDto dto);

  AttendanceDto update(Long id, AttendanceDto dto);

  void delete(Long id);

  AttendanceDto getById(Long id);

  List<AttendanceDto> getAll();
}
