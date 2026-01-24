/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;

import com.pharmacy.dto.PurchasePaymentDto;

public interface PurchasePaymentService {
  PurchasePaymentDto create(PurchasePaymentDto dto);

  PurchasePaymentDto update(Long id, PurchasePaymentDto dto);

  void delete(Long id);

  PurchasePaymentDto getById(Long id);

  List<PurchasePaymentDto> getAll();

  PurchasePaymentDto changeStatus(Long id, String status);
}
