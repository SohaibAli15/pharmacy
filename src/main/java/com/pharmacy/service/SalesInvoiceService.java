/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;

import com.pharmacy.dto.SalesInvoiceDto;

public interface SalesInvoiceService {
  SalesInvoiceDto create(SalesInvoiceDto dto);

  SalesInvoiceDto update(Long id, SalesInvoiceDto dto);

  void delete(Long id);

  SalesInvoiceDto getById(Long id);

  List<SalesInvoiceDto> getAll();

  SalesInvoiceDto changeStatus(Long id, String status);
}
