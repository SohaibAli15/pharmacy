/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.entity.Dispatch;
import com.pharmacy.service.DispatchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/dispatches")
@RequiredArgsConstructor
public class DispatchController {
  private final DispatchService dispatchService;

  @GetMapping
  public List<Dispatch> getAll() {
    return dispatchService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Dispatch> getById(@PathVariable Long id) {
    return dispatchService
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Dispatch create(@RequestBody Dispatch dispatch) {
    return dispatchService.save(dispatch);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Dispatch> update(@PathVariable Long id, @RequestBody Dispatch dispatch) {
    return dispatchService
        .findById(id)
        .map(
            existing -> {
              dispatch.setId(id);
              return ResponseEntity.ok(dispatchService.save(dispatch));
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    dispatchService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
