/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.entity.MaterialIssue;
import com.pharmacy.service.MaterialIssueService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/material-issues")
@RequiredArgsConstructor
public class MaterialIssueController {
  private final MaterialIssueService materialIssueService;

  @GetMapping
  public List<MaterialIssue> getAll() {
    return materialIssueService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<MaterialIssue> getById(@PathVariable Long id) {
    return materialIssueService
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public MaterialIssue create(@RequestBody MaterialIssue materialIssue) {
    return materialIssueService.save(materialIssue);
  }

  @PutMapping("/{id}")
  public ResponseEntity<MaterialIssue> update(
      @PathVariable Long id, @RequestBody MaterialIssue materialIssue) {
    return materialIssueService
        .findById(id)
        .map(
            existing -> {
              materialIssue.setId(id);
              return ResponseEntity.ok(materialIssueService.save(materialIssue));
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    materialIssueService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
