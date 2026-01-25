/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.entity.WorkOrder;
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

  public WorkOrder save(WorkOrder workOrder) {
    return workOrderRepository.save(workOrder);
  }

  public void deleteById(Long id) {
    workOrderRepository.deleteById(id);
  }
}
