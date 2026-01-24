/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.pharmacy.dto.JournalVoucherDto;
import com.pharmacy.entity.JournalVoucher;
import com.pharmacy.repository.JournalVoucherRepository;
import com.pharmacy.service.JournalVoucherService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JournalVoucherServiceImpl implements JournalVoucherService {
  private final JournalVoucherRepository repository;

  @Override
  public JournalVoucherDto create(JournalVoucherDto dto) {
    JournalVoucher entity = new JournalVoucher();
    BeanUtils.copyProperties(dto, entity);
    JournalVoucher saved = repository.save(entity);
    JournalVoucherDto result = new JournalVoucherDto();
    BeanUtils.copyProperties(saved, result);
    return result;
  }

  @Override
  public JournalVoucherDto update(Long id, JournalVoucherDto dto) {
    JournalVoucher entity = repository.findById(id).orElseThrow();
    BeanUtils.copyProperties(dto, entity, "id", "voucherId");
    JournalVoucher saved = repository.save(entity);
    JournalVoucherDto result = new JournalVoucherDto();
    BeanUtils.copyProperties(saved, result);
    return result;
  }

  @Override
  public void delete(Long id) {
    repository.deleteById(id);
  }

  @Override
  public JournalVoucherDto getById(Long id) {
    JournalVoucher entity = repository.findById(id).orElseThrow();
    JournalVoucherDto dto = new JournalVoucherDto();
    BeanUtils.copyProperties(entity, dto);
    return dto;
  }

  @Override
  public List<JournalVoucherDto> getAll() {
    return repository.findAll().stream()
        .map(
            entity -> {
              JournalVoucherDto dto = new JournalVoucherDto();
              BeanUtils.copyProperties(entity, dto);
              return dto;
            })
        .collect(Collectors.toList());
  }

  @Override
  public List<JournalVoucherDto> getByCompany(String company) {
    return repository.findByCompany(company).stream()
        .map(
            entity -> {
              JournalVoucherDto dto = new JournalVoucherDto();
              BeanUtils.copyProperties(entity, dto);
              return dto;
            })
        .collect(Collectors.toList());
  }
}
