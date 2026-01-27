/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.ProductionBatchDto;
import com.pharmacy.dto.SalesOrderDto;
import com.pharmacy.dto.WorkOrderDto;
import com.pharmacy.entity.ProductionBatch;
import com.pharmacy.entity.SalesOrder;
import com.pharmacy.entity.WorkOrder;
import com.pharmacy.service.ProductionBatchService;
import com.pharmacy.service.SalesOrderService;
import com.pharmacy.service.WorkOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/work-orders")
@RequiredArgsConstructor
public class WorkOrderController {
  private final WorkOrderService workOrderService;
  private final SalesOrderService salesOrderService;
  private final ProductionBatchService productionBatchService;

  @GetMapping
  public List<WorkOrderDto> getAll() {
    return workOrderService.findAll().stream().map(workOrderService::mapToDto).toList();
  }

  @GetMapping("/{id}")
  public ResponseEntity<WorkOrderDto> getById(@PathVariable Long id) {
    return workOrderService
        .findById(id)
        .map(workOrderService::mapToDto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<WorkOrderDto> create(@RequestBody WorkOrderDto workOrderDto) {
    SalesOrder salesOrder = null;
    if (workOrderDto.getSalesOrderId() != null) {
      SalesOrderDto salesOrderDto = salesOrderService.getById(workOrderDto.getSalesOrderId());
      salesOrder = new SalesOrder();
      salesOrder.setId(salesOrderDto.getId());
    }
    List<ProductionBatch> batches = null;
    if (workOrderDto.getProductionBatchIds() != null
        && !workOrderDto.getProductionBatchIds().isEmpty()) {
      batches =
          workOrderDto.getProductionBatchIds().stream()
              .map(
                  id -> {
                    ProductionBatchDto batchDto = productionBatchService.getById(id);
                    ProductionBatch batch = new ProductionBatch();
                    batch.setId(batchDto.getId());
                    return batch;
                  })
              .toList();
    }
    WorkOrder workOrder = workOrderService.mapToEntity(workOrderDto, salesOrder, batches);
    WorkOrder savedWorkOrder = workOrderService.save(workOrder);
    return ResponseEntity.ok(workOrderService.mapToDto(savedWorkOrder));
  }

  @PutMapping("/{id}")
  public ResponseEntity<WorkOrderDto> update(
      @PathVariable Long id, @RequestBody WorkOrderDto workOrderDto) {
    return workOrderService
        .findById(id)
        .map(
            existing -> {
              SalesOrder salesOrder = null;
              if (workOrderDto.getSalesOrderId() != null) {
                SalesOrderDto salesOrderDto =
                    salesOrderService.getById(workOrderDto.getSalesOrderId());
                salesOrder = new SalesOrder();
                salesOrder.setId(salesOrderDto.getId());
              }
              List<ProductionBatch> batches = null;
              if (workOrderDto.getProductionBatchIds() != null
                  && !workOrderDto.getProductionBatchIds().isEmpty()) {
                batches =
                    workOrderDto.getProductionBatchIds().stream()
                        .map(
                            batchId -> {
                              ProductionBatchDto batchDto = productionBatchService.getById(batchId);
                              ProductionBatch batch = new ProductionBatch();
                              batch.setId(batchDto.getId());
                              return batch;
                            })
                        .toList();
              }
              workOrderService.updateEntityFromDto(existing, workOrderDto, salesOrder, batches);
              WorkOrder updated = workOrderService.save(existing);
              return ResponseEntity.ok(workOrderService.mapToDto(updated));
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    workOrderService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
