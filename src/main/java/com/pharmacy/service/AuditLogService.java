/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.entity.AuditLog;
import com.pharmacy.repository.AuditLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditLogService {
  private final AuditLogRepository auditLogRepository;

  public List<AuditLog> findAll() {
    return auditLogRepository.findAll();
  }

  public Optional<AuditLog> findById(Long id) {
    return auditLogRepository.findById(id);
  }

  public AuditLog save(AuditLog auditLog) {
    return auditLogRepository.save(auditLog);
  }

  public void deleteById(Long id) {
    auditLogRepository.deleteById(id);
  }
}
