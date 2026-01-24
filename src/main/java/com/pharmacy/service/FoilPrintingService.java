/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;

import com.pharmacy.dto.FoilPrintingDto;

public interface FoilPrintingService {
  FoilPrintingDto create(FoilPrintingDto dto);

  FoilPrintingDto update(Long id, FoilPrintingDto dto);

  void delete(Long id);

  FoilPrintingDto getById(Long id);

  List<FoilPrintingDto> getAll();
}
