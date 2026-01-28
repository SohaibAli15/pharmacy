/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.ProductionBatchDto;
import com.pharmacy.dto.SaleDto;
import com.pharmacy.dto.WorkOrderDto;
import com.pharmacy.entity.ProductionBatch;
import com.pharmacy.entity.WorkOrder;
import com.pharmacy.service.ProductionBatchService;
import com.pharmacy.service.SaleService;
import com.pharmacy.service.WorkOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/work-orders")
@RequiredArgsConstructor
public class WorkOrderController {
  private final WorkOrderService workOrderService;
  private final SaleService saleService;
  private final ProductionBatchService productionBatchService;

  @GetMapping
  public List<WorkOrderDto> getAll() {
    return workOrderService.findAllWithBatches().stream().map(workOrderService::mapToDto).toList();
  }

  @GetMapping("/{id}")
  public ResponseEntity<WorkOrderDto> getById(@PathVariable Long id) {
    return workOrderService
        .findByIdWithBatches(id)
        .map(workOrderService::mapToDto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<WorkOrderDto> create(@RequestBody WorkOrderDto workOrderDto) {
    SaleDto sale = null;
    if (workOrderDto.getSalesOrderId() != null) {
      sale = saleService.getSaleById(workOrderDto.getSalesOrderId());
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
    WorkOrder workOrder = workOrderService.mapToEntity(workOrderDto, sale, batches);
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
              SaleDto sale = null;
              if (workOrderDto.getSalesOrderId() != null) {
                sale = saleService.getSaleById(workOrderDto.getSalesOrderId());
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
              workOrderService.updateEntityFromDto(existing, workOrderDto, sale, batches);
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
