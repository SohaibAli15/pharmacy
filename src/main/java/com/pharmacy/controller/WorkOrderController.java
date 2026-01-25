/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.entity.WorkOrder;
import com.pharmacy.service.WorkOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/work-orders")
@RequiredArgsConstructor
public class WorkOrderController {
  private final WorkOrderService workOrderService;

  @GetMapping
  public List<WorkOrder> getAll() {
    return workOrderService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<WorkOrder> getById(@PathVariable Long id) {
    return workOrderService
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public WorkOrder create(@RequestBody WorkOrder workOrder) {
    return workOrderService.save(workOrder);
  }

  @PutMapping("/{id}")
  public ResponseEntity<WorkOrder> update(@PathVariable Long id, @RequestBody WorkOrder workOrder) {
    return workOrderService
        .findById(id)
        .map(
            existing -> {
              workOrder.setId(id);
              return ResponseEntity.ok(workOrderService.save(workOrder));
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    workOrderService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
