/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;

import com.pharmacy.dto.JournalVoucherDto;

public interface JournalVoucherService {
  JournalVoucherDto create(JournalVoucherDto dto);

  JournalVoucherDto update(Long id, JournalVoucherDto dto);

  void delete(Long id);

  JournalVoucherDto getById(Long id);

  List<JournalVoucherDto> getAll();

  List<JournalVoucherDto> getByCompany(String company);
}
