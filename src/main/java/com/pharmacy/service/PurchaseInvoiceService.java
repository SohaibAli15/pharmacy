/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;

import com.pharmacy.dto.PurchaseInvoiceDto;

public interface PurchaseInvoiceService {
  PurchaseInvoiceDto create(PurchaseInvoiceDto dto);

  PurchaseInvoiceDto update(Long id, PurchaseInvoiceDto dto);

  void delete(Long id);

  PurchaseInvoiceDto getById(Long id);

  List<PurchaseInvoiceDto> getAll();

  PurchaseInvoiceDto changeStatus(Long id, String status);
}
