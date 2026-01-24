/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.pharmacy.dto.GeneralLedgerEntryDto;
import com.pharmacy.entity.GeneralLedgerEntry;
import com.pharmacy.repository.GeneralLedgerEntryRepository;
import com.pharmacy.service.GeneralLedgerEntryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeneralLedgerEntryServiceImpl implements GeneralLedgerEntryService {
  private final GeneralLedgerEntryRepository repository;

  @Override
  public GeneralLedgerEntryDto create(GeneralLedgerEntryDto dto) {
    GeneralLedgerEntry entity = new GeneralLedgerEntry();
    BeanUtils.copyProperties(dto, entity);
    GeneralLedgerEntry saved = repository.save(entity);
    GeneralLedgerEntryDto result = new GeneralLedgerEntryDto();
    BeanUtils.copyProperties(saved, result);
    return result;
  }

  @Override
  public GeneralLedgerEntryDto update(Long id, GeneralLedgerEntryDto dto) {
    GeneralLedgerEntry entity = repository.findById(id).orElseThrow();
    BeanUtils.copyProperties(dto, entity, "id", "entryId");
    GeneralLedgerEntry saved = repository.save(entity);
    GeneralLedgerEntryDto result = new GeneralLedgerEntryDto();
    BeanUtils.copyProperties(saved, result);
    return result;
  }

  @Override
  public void delete(Long id) {
    repository.deleteById(id);
  }

  @Override
  public GeneralLedgerEntryDto getById(Long id) {
    GeneralLedgerEntry entity = repository.findById(id).orElseThrow();
    GeneralLedgerEntryDto dto = new GeneralLedgerEntryDto();
    BeanUtils.copyProperties(entity, dto);
    return dto;
  }

  @Override
  public List<GeneralLedgerEntryDto> getAll() {
    return repository.findAll().stream()
        .map(
            entity -> {
              GeneralLedgerEntryDto dto = new GeneralLedgerEntryDto();
              BeanUtils.copyProperties(entity, dto);
              return dto;
            })
        .collect(Collectors.toList());
  }

  @Override
  public List<GeneralLedgerEntryDto> getByCompany(String company) {
    return repository.findByCompany(company).stream()
        .map(
            entity -> {
              GeneralLedgerEntryDto dto = new GeneralLedgerEntryDto();
              BeanUtils.copyProperties(entity, dto);
              return dto;
            })
        .collect(Collectors.toList());
  }
}
