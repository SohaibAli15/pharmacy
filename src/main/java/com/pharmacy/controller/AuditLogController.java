/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.entity.AuditLog;
import com.pharmacy.service.AuditLogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {
  private final AuditLogService auditLogService;

  @GetMapping
  public List<AuditLog> getAll() {
    return auditLogService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<AuditLog> getById(@PathVariable Long id) {
    return auditLogService
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public AuditLog create(@RequestBody AuditLog auditLog) {
    return auditLogService.save(auditLog);
  }

  @PutMapping("/{id}")
  public ResponseEntity<AuditLog> update(@PathVariable Long id, @RequestBody AuditLog auditLog) {
    return auditLogService
        .findById(id)
        .map(
            existing -> {
              auditLog.setId(id);
              return ResponseEntity.ok(auditLogService.save(auditLog));
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    auditLogService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
