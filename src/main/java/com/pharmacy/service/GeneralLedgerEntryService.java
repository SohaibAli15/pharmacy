/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;

import com.pharmacy.dto.GeneralLedgerEntryDto;

public interface GeneralLedgerEntryService {
  GeneralLedgerEntryDto create(GeneralLedgerEntryDto dto);

  GeneralLedgerEntryDto update(Long id, GeneralLedgerEntryDto dto);

  void delete(Long id);

  GeneralLedgerEntryDto getById(Long id);

  List<GeneralLedgerEntryDto> getAll();

  List<GeneralLedgerEntryDto> getByCompany(String company);
}
