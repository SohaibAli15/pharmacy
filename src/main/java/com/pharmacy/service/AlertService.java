/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.AlertDto;
import com.pharmacy.entity.Alert;
import com.pharmacy.entity.AlertTypeEntity;
import com.pharmacy.repository.AlertRepository;
import com.pharmacy.repository.AlertTypeRepository;
import com.pharmacy.repository.IngredientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertService {

  private final AlertRepository alertRepository;
  private final IngredientRepository ingredientRepository;
  private final AlertTypeRepository alertTypeRepository;

  public AlertDto toDto(Alert a) {
    AlertDto dto = new AlertDto();
    dto.setId(a.getId());
    dto.setIngredientId(a.getIngredient().getId());
    dto.setIngredientName(a.getIngredient().getName());
    dto.setAlertType(a.getAlertType().getName());
    dto.setMessage(a.getMessage());
    dto.setTimestamp(a.getTimestamp());
    dto.setIsRead(a.getIsRead());
    return dto;
  }

  public Alert fromDto(AlertDto dto) {
    Alert a = new Alert();
    a.setId(dto.getId());
    a.setIngredient(ingredientRepository.findById(dto.getIngredientId()).orElseThrow());
    AlertTypeEntity alertType =
        alertTypeRepository
            .findByName(dto.getAlertType())
            .orElseThrow(() -> new RuntimeException("AlertType not found: " + dto.getAlertType()));
    a.setAlertType(alertType);
    a.setMessage(dto.getMessage());
    a.setTimestamp(dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now());
    a.setIsRead(dto.getIsRead() != null ? dto.getIsRead() : false);
    return a;
  }

  public AlertDto create(AlertDto dto) {
    Alert a = fromDto(dto);
    Alert saved = alertRepository.save(a);
    return toDto(saved);
  }

  public void markAsRead(Long id) {
    Alert existing = alertRepository.findById(id).orElseThrow();
    existing.setIsRead(true);
    alertRepository.save(existing);
  }

  public void delete(Long id) {
    alertRepository.deleteById(id);
  }

  public AlertDto getById(Long id) {
    return alertRepository.findById(id).map(this::toDto).orElse(null);
  }

  public Page<AlertDto> getUnreadAlerts(Pageable pageable) {
    return alertRepository.findByIsReadFalse(pageable).map(this::toDto);
  }

  public Page<AlertDto> getByIngredient(Long ingredientId, Pageable pageable) {
    return alertRepository.findByIngredientId(ingredientId, pageable).map(this::toDto);
  }

  public Page<AlertDto> listAll(Pageable pageable) {
    return alertRepository.findAll(pageable).map(this::toDto);
  }

  // Backwards compatible List methods (optional) kept small by delegating to pageable
  public List<AlertDto> getUnreadAlerts() {
    return getUnreadAlerts(org.springframework.data.domain.PageRequest.of(0, 20)).getContent();
  }

  public List<AlertDto> getByIngredient(Long ingredientId) {
    return getByIngredient(ingredientId, org.springframework.data.domain.PageRequest.of(0, 20))
        .getContent();
  }

  public List<AlertDto> listAll() {
    return listAll(org.springframework.data.domain.PageRequest.of(0, 20)).getContent();
  }
}
