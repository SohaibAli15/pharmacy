/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.SaleDto;
import com.pharmacy.dto.WorkOrderDto;
import com.pharmacy.entity.ProductionBatch;
import com.pharmacy.entity.Sale;
import com.pharmacy.entity.WorkOrder;
import com.pharmacy.exception.NotFoundException;
import com.pharmacy.repository.WorkOrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkOrderService {
  private final WorkOrderRepository workOrderRepository;

  public List<WorkOrder> findAll() {
    return workOrderRepository.findAll();
  }

  public Optional<WorkOrder> findById(Long id) {
    return workOrderRepository.findById(id);
  }

  public WorkOrderDto getById(Long id) {
    return workOrderRepository
        .findById(id)
        .map(this::mapToDto)
        .orElseThrow(() -> new NotFoundException("WorkOrder not found: " + id));
  }

  public WorkOrder save(WorkOrder workOrder) {
    return workOrderRepository.save(workOrder);
  }

  public void deleteById(Long id) {
    workOrderRepository.deleteById(id);
  }

  public WorkOrderDto mapToDto(WorkOrder workOrder) {
    WorkOrderDto dto = new WorkOrderDto();
    dto.setId(workOrder.getId());
    dto.setWorkOrderNumber(workOrder.getWorkOrderNumber());
    dto.setCreatedAt(workOrder.getCreatedAt());
    dto.setStatus(workOrder.getStatus() != null ? workOrder.getStatus().name() : null);
    dto.setNotes(workOrder.getNotes());
    if (workOrder.getSale() != null) {
      dto.setSalesOrderId(workOrder.getSale().getId());
    }
    if (workOrder.getProductionBatches() != null) {
      dto.setProductionBatchIds(
          workOrder.getProductionBatches().stream()
              .map(com.pharmacy.entity.ProductionBatch::getId)
              .collect(java.util.stream.Collectors.toList()));
    }
    return dto;
  }

  public WorkOrder mapToEntity(WorkOrderDto dto, SaleDto saleDto, List<ProductionBatch> batches) {
    WorkOrder workOrder = new WorkOrder();
    workOrder.setId(dto.getId());
    workOrder.setWorkOrderNumber(dto.getWorkOrderNumber());
    if (dto.getCreatedAt() != null) {
      workOrder.setCreatedAt(dto.getCreatedAt());
    } else {
      workOrder.setCreatedAt(java.time.LocalDateTime.now());
    }
    workOrder.setStatus(
        dto.getStatus() != null
            ? WorkOrder.Status.valueOf(dto.getStatus())
            : WorkOrder.Status.CREATED);
    workOrder.setNotes(dto.getNotes());
    if (saleDto != null) {
      Sale sale = new Sale();
      sale.setId(saleDto.getId());
      workOrder.setSale(sale);
    }
    workOrder.setProductionBatches(batches);
    return workOrder;
  }

  public void updateEntityFromDto(
      WorkOrder workOrder, WorkOrderDto dto, SaleDto saleDto, List<ProductionBatch> batches) {
    workOrder.setWorkOrderNumber(dto.getWorkOrderNumber());
    if (dto.getCreatedAt() != null) {
      workOrder.setCreatedAt(dto.getCreatedAt());
    } else {
      workOrder.setCreatedAt(java.time.LocalDateTime.now());
    }
    workOrder.setStatus(
        dto.getStatus() != null
            ? WorkOrder.Status.valueOf(dto.getStatus())
            : WorkOrder.Status.CREATED);
    workOrder.setNotes(dto.getNotes());
    if (saleDto != null) {
      Sale sale = new Sale();
      sale.setId(saleDto.getId());
      workOrder.setSale(sale);
    }
    workOrder.setProductionBatches(batches);
  }

  public Optional<WorkOrder> findByIdWithBatches(Long id) {
    return workOrderRepository.findByIdWithBatches(id);
  }

  public List<WorkOrder> findAllWithBatches() {
    return workOrderRepository.findAllWithBatches();
  }
}
