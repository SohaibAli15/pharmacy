/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;

import com.pharmacy.dto.SalesOrderDto;

public interface SalesOrderService {
  SalesOrderDto create(SalesOrderDto dto);

  SalesOrderDto update(Long id, SalesOrderDto dto);

  void delete(Long id);

  SalesOrderDto getById(Long id);

  List<SalesOrderDto> getAll();

  SalesOrderDto changeStatus(Long id, String status);
}
