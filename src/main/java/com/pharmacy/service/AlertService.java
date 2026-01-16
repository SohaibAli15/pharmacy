/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.AlertDto;
import com.pharmacy.entity.Alert;
import com.pharmacy.repository.AlertRepository;
import com.pharmacy.repository.IngredientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertService {

  private final AlertRepository alertRepository;
  private final IngredientRepository ingredientRepository;

  public AlertDto toDto(Alert a) {
    AlertDto dto = new AlertDto();
    dto.setId(a.getId());
    dto.setIngredientId(a.getIngredient().getId());
    dto.setIngredientName(a.getIngredient().getName());
    dto.setAlertType(a.getAlertType().name());
    dto.setMessage(a.getMessage());
    dto.setTimestamp(a.getTimestamp());
    dto.setIsRead(a.getIsRead());
    return dto;
  }

  public Alert fromDto(AlertDto dto) {
    Alert a = new Alert();
    a.setId(dto.getId());
    a.setIngredient(ingredientRepository.findById(dto.getIngredientId()).orElseThrow());
    a.setAlertType(Alert.AlertType.valueOf(dto.getAlertType()));
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

  public List<AlertDto> getUnreadAlerts() {
    return alertRepository.findByIsReadFalse().stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public List<AlertDto> getByIngredient(Long ingredientId) {
    return alertRepository.findByIngredientId(ingredientId).stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public List<AlertDto> listAll() {
    return alertRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
  }
}
