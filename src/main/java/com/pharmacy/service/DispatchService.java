/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.DispatchDto;
import com.pharmacy.entity.Dispatch;
import com.pharmacy.entity.Sale;
import com.pharmacy.repository.DispatchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DispatchService {
  private final DispatchRepository dispatchRepository;

  public List<Dispatch> findAll() {
    return dispatchRepository.findAll();
  }

  public Optional<Dispatch> findById(Long id) {
    return dispatchRepository.findById(id);
  }

  public Dispatch save(Dispatch dispatch) {
    return dispatchRepository.save(dispatch);
  }

  public void deleteById(Long id) {
    dispatchRepository.deleteById(id);
  }

  public List<DispatchDto> findAllDto() {
    return dispatchRepository.findAll().stream().map(this::toDto).toList();
  }

  public java.util.Optional<DispatchDto> findDtoById(Long id) {
    return dispatchRepository.findById(id).map(this::toDto);
  }

  public DispatchDto saveDto(DispatchDto dto) {
    Dispatch entity = fromDto(dto);
    Dispatch saved = dispatchRepository.save(entity);
    return toDto(saved);
  }

  private DispatchDto toDto(Dispatch d) {
    DispatchDto dto = new DispatchDto();
    dto.setId(d.getId());
    dto.setSaleId(d.getSale() != null ? d.getSale().getId() : null);
    dto.setDispatchDate(d.getDispatchDate());
    dto.setQuantityDispatched(d.getQuantityDispatched());
    dto.setStatus(d.getStatus() != null ? d.getStatus().name() : null);
    return dto;
  }

  private Dispatch fromDto(DispatchDto dto) {
    Dispatch d = new Dispatch();
    d.setId(dto.getId());
    if (dto.getSaleId() != null) {
      Sale sale = new Sale();
      sale.setId(dto.getSaleId());
      d.setSale(sale);
    }
    d.setDispatchDate(dto.getDispatchDate());
    d.setQuantityDispatched(dto.getQuantityDispatched());
    if (dto.getStatus() != null) {
      d.setStatus(Dispatch.Status.valueOf(dto.getStatus()));
    }
    return d;
  }
}
