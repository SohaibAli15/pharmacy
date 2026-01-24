/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.pharmacy.dto.AdjustmentNoteDto;
import com.pharmacy.entity.AdjustmentNote;
import com.pharmacy.repository.AdjustmentNoteRepository;
import com.pharmacy.service.AdjustmentNoteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdjustmentNoteServiceImpl implements AdjustmentNoteService {
  private final AdjustmentNoteRepository repository;

  @Override
  public AdjustmentNoteDto create(AdjustmentNoteDto dto) {
    AdjustmentNote entity = new AdjustmentNote();
    BeanUtils.copyProperties(dto, entity);
    AdjustmentNote saved = repository.save(entity);
    AdjustmentNoteDto result = new AdjustmentNoteDto();
    BeanUtils.copyProperties(saved, result);
    return result;
  }

  @Override
  public AdjustmentNoteDto update(Long id, AdjustmentNoteDto dto) {
    AdjustmentNote entity = repository.findById(id).orElseThrow();
    BeanUtils.copyProperties(dto, entity, "id", "noteId");
    AdjustmentNote saved = repository.save(entity);
    AdjustmentNoteDto result = new AdjustmentNoteDto();
    BeanUtils.copyProperties(saved, result);
    return result;
  }

  @Override
  public void delete(Long id) {
    repository.deleteById(id);
  }

  @Override
  public AdjustmentNoteDto getById(Long id) {
    AdjustmentNote entity = repository.findById(id).orElseThrow();
    AdjustmentNoteDto dto = new AdjustmentNoteDto();
    BeanUtils.copyProperties(entity, dto);
    return dto;
  }

  @Override
  public List<AdjustmentNoteDto> getAll() {
    return repository.findAll().stream()
        .map(
            entity -> {
              AdjustmentNoteDto dto = new AdjustmentNoteDto();
              BeanUtils.copyProperties(entity, dto);
              return dto;
            })
        .collect(Collectors.toList());
  }

  @Override
  public List<AdjustmentNoteDto> getByCompany(String company) {
    return repository.findByCompany(company).stream()
        .map(
            entity -> {
              AdjustmentNoteDto dto = new AdjustmentNoteDto();
              BeanUtils.copyProperties(entity, dto);
              return dto;
            })
        .collect(Collectors.toList());
  }
}
