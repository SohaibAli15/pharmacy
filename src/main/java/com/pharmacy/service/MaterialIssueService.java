/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.MaterialIssueDto;
import com.pharmacy.entity.MaterialIssue;
import com.pharmacy.entity.ProductionBatch;
import com.pharmacy.entity.SalesOrder;
import com.pharmacy.repository.MaterialIssueRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MaterialIssueService {
  private final MaterialIssueRepository materialIssueRepository;

  public List<MaterialIssue> findAll() {
    return materialIssueRepository.findAll();
  }

  public Optional<MaterialIssue> findById(Long id) {
    return materialIssueRepository.findById(id);
  }

  public MaterialIssue save(MaterialIssue materialIssue) {
    return materialIssueRepository.save(materialIssue);
  }

  public void deleteById(Long id) {
    materialIssueRepository.deleteById(id);
  }

  public List<MaterialIssueDto> findAllDto() {
    return materialIssueRepository.findAll().stream().map(this::toDto).toList();
  }

  public java.util.Optional<MaterialIssueDto> findDtoById(Long id) {
    return materialIssueRepository.findById(id).map(this::toDto);
  }

  public MaterialIssueDto saveDto(MaterialIssueDto dto) {
    MaterialIssue entity = fromDto(dto);
    MaterialIssue saved = materialIssueRepository.save(entity);
    return toDto(saved);
  }

  private MaterialIssueDto toDto(MaterialIssue m) {
    MaterialIssueDto dto = new MaterialIssueDto();
    dto.setId(m.getId());
    dto.setProductionBatchId(
        m.getProductionBatch() != null ? m.getProductionBatch().getId() : null);
    dto.setSalesOrderId(m.getSalesOrder() != null ? m.getSalesOrder().getId() : null);
    dto.setIssueDate(m.getIssueDate());
    dto.setTotalQuantityIssued(m.getTotalQuantityIssued());
    dto.setStatus(m.getStatus() != null ? m.getStatus().name() : null);
    return dto;
  }

  private MaterialIssue fromDto(MaterialIssueDto dto) {
    MaterialIssue m = new MaterialIssue();
    m.setId(dto.getId());
    if (dto.getProductionBatchId() != null) {
      ProductionBatch pb = new ProductionBatch();
      pb.setId(dto.getProductionBatchId());
      m.setProductionBatch(pb);
    }
    if (dto.getSalesOrderId() != null) {
      SalesOrder so = new SalesOrder();
      so.setId(dto.getSalesOrderId());
      m.setSalesOrder(so);
    }
    m.setIssueDate(dto.getIssueDate());
    m.setTotalQuantityIssued(dto.getTotalQuantityIssued());
    if (dto.getStatus() != null) {
      m.setStatus(MaterialIssue.Status.valueOf(dto.getStatus()));
    }
    return m;
  }
}
