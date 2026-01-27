/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.DispatchDto;
import com.pharmacy.service.DispatchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/dispatches")
@RequiredArgsConstructor
public class DispatchController {
  private final DispatchService dispatchService;

  @GetMapping
  public List<DispatchDto> getAll() {
    return dispatchService.findAllDto();
  }

  @GetMapping("/{id}")
  public ResponseEntity<DispatchDto> getById(@PathVariable Long id) {
    return dispatchService
        .findDtoById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public DispatchDto create(@RequestBody DispatchDto dispatch) {
    return dispatchService.saveDto(dispatch);
  }

  @PutMapping("/{id}")
  public ResponseEntity<DispatchDto> update(
      @PathVariable Long id, @RequestBody DispatchDto dispatch) {
    return dispatchService
        .findDtoById(id)
        .map(
            existing -> {
              dispatch.setId(id);
              return ResponseEntity.ok(dispatchService.saveDto(dispatch));
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    dispatchService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
