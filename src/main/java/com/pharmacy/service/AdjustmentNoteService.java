/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;

import com.pharmacy.dto.AdjustmentNoteDto;

public interface AdjustmentNoteService {
  AdjustmentNoteDto create(AdjustmentNoteDto dto);

  AdjustmentNoteDto update(Long id, AdjustmentNoteDto dto);

  void delete(Long id);

  AdjustmentNoteDto getById(Long id);

  List<AdjustmentNoteDto> getAll();

  List<AdjustmentNoteDto> getByCompany(String company);
}
