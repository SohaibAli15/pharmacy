/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.pharmacy.dto.FoilPrintingDto;
import com.pharmacy.entity.FoilPrinting;
import com.pharmacy.repository.FoilPrintingRepository;
import com.pharmacy.service.FoilPrintingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoilPrintingServiceImpl implements FoilPrintingService {
  private final FoilPrintingRepository foilPrintingRepository;

  @Override
  public FoilPrintingDto create(FoilPrintingDto dto) {
    FoilPrinting entity = new FoilPrinting();
    BeanUtils.copyProperties(dto, entity);
    FoilPrinting saved = foilPrintingRepository.save(entity);
    FoilPrintingDto result = new FoilPrintingDto();
    BeanUtils.copyProperties(saved, result);
    return result;
  }

  @Override
  public FoilPrintingDto update(Long id, FoilPrintingDto dto) {
    FoilPrinting entity = foilPrintingRepository.findById(id).orElseThrow();
    BeanUtils.copyProperties(dto, entity, "id", "createdAt");
    FoilPrinting saved = foilPrintingRepository.save(entity);
    FoilPrintingDto result = new FoilPrintingDto();
    BeanUtils.copyProperties(saved, result);
    return result;
  }

  @Override
  public void delete(Long id) {
    foilPrintingRepository.deleteById(id);
  }

  @Override
  public FoilPrintingDto getById(Long id) {
    FoilPrinting entity = foilPrintingRepository.findById(id).orElseThrow();
    FoilPrintingDto dto = new FoilPrintingDto();
    BeanUtils.copyProperties(entity, dto);
    return dto;
  }

  @Override
  public List<FoilPrintingDto> getAll() {
    return foilPrintingRepository.findAll().stream()
        .map(
            entity -> {
              FoilPrintingDto dto = new FoilPrintingDto();
              BeanUtils.copyProperties(entity, dto);
              return dto;
            })
        .collect(Collectors.toList());
  }
}
