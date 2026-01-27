/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.MaterialIssueDto;
import com.pharmacy.service.MaterialIssueService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/material-issues")
@RequiredArgsConstructor
public class MaterialIssueController {
  private final MaterialIssueService materialIssueService;

  @GetMapping
  public List<MaterialIssueDto> getAll() {
    return materialIssueService.findAllDto();
  }

  @GetMapping("/{id}")
  public ResponseEntity<MaterialIssueDto> getById(@PathVariable Long id) {
    return materialIssueService
        .findDtoById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public MaterialIssueDto create(@RequestBody MaterialIssueDto materialIssue) {
    return materialIssueService.saveDto(materialIssue);
  }

  @PutMapping("/{id}")
  public ResponseEntity<MaterialIssueDto> update(
      @PathVariable Long id, @RequestBody MaterialIssueDto materialIssue) {
    return materialIssueService
        .findDtoById(id)
        .map(
            existing -> {
              materialIssue.setId(id);
              return ResponseEntity.ok(materialIssueService.saveDto(materialIssue));
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    materialIssueService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
