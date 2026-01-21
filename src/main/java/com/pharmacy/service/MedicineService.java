/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.MedicineDto;
import com.pharmacy.entity.Medicine;
import com.pharmacy.repository.MedicineRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicineService {

  private final MedicineRepository medicineRepository;

  public MedicineDto toDto(Medicine m) {
    MedicineDto dto = new MedicineDto();
    dto.setId(m.getId());
    dto.setName(m.getName());
    dto.setDescription(m.getDescription());
    dto.setManufacturer(m.getManufacturer());
    dto.setPrice(m.getPrice());
    dto.setStockQuantity(m.getStockQuantity());
    dto.setExpiryDate(m.getExpiryDate());
    dto.setCategory(m.getCategory());
    return dto;
  }

  public Medicine fromDto(MedicineDto dto) {
    Medicine m = new Medicine();
    m.setId(dto.getId());
    m.setName(dto.getName());
    m.setDescription(dto.getDescription());
    m.setManufacturer(dto.getManufacturer());
    m.setPrice(dto.getPrice());
    m.setStockQuantity(dto.getStockQuantity());
    m.setExpiryDate(dto.getExpiryDate());
    m.setCategory(dto.getCategory());
    return m;
  }

  public MedicineDto create(MedicineDto dto) {
    Medicine m = fromDto(dto);
    Medicine saved = medicineRepository.save(m);
    return toDto(saved);
  }

  public MedicineDto update(Long id, MedicineDto dto) {
    Medicine existing = medicineRepository.findById(id).orElseThrow();
    existing.setName(dto.getName());
    existing.setDescription(dto.getDescription());
    existing.setManufacturer(dto.getManufacturer());
    existing.setPrice(dto.getPrice());
    existing.setStockQuantity(dto.getStockQuantity());
    existing.setExpiryDate(dto.getExpiryDate());
    existing.setCategory(dto.getCategory());
    Medicine saved = medicineRepository.save(existing);
    return toDto(saved);
  }

  public void delete(Long id) {
    medicineRepository.deleteById(id);
  }

  public MedicineDto getById(Long id) {
    return medicineRepository.findById(id).map(this::toDto).orElse(null);
  }

  public Page<MedicineDto> searchByName(String name, Pageable pageable) {
    return medicineRepository.findByNameContainingIgnoreCase(name, pageable).map(this::toDto);
  }

  public Page<MedicineDto> findByCategory(String category, Pageable pageable) {
    return medicineRepository.findByCategory(category, pageable).map(this::toDto);
  }

  public Page<MedicineDto> listAll(Pageable pageable) {
    return medicineRepository.findAll(pageable).map(this::toDto);
  }
}
