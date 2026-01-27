/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;

import com.pharmacy.dto.GoodsReceiptNoteDto;

public interface GoodsReceiptNoteService {
  GoodsReceiptNoteDto create(GoodsReceiptNoteDto dto);

  GoodsReceiptNoteDto update(Long id, GoodsReceiptNoteDto dto);

  void delete(Long id);

  GoodsReceiptNoteDto getById(Long id);

  List<GoodsReceiptNoteDto> getAll();

  GoodsReceiptNoteDto changeStatus(Long id, String status);

  List<GoodsReceiptNoteDto> findAllDto();

  java.util.Optional<GoodsReceiptNoteDto> findDtoById(Long id);

  GoodsReceiptNoteDto saveDto(GoodsReceiptNoteDto dto);
}
