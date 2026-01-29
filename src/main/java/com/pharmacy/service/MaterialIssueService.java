/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.MaterialIssueDto;
import com.pharmacy.entity.MaterialIssue;
import com.pharmacy.entity.MaterialIssueItem;
import com.pharmacy.entity.ProductionBatch;
import com.pharmacy.entity.Sale;
import com.pharmacy.repository.MaterialIssueRepository;
import com.pharmacy.repository.ProductionBatchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MaterialIssueService {
  private final MaterialIssueRepository materialIssueRepository;
  private final ProductionBatchRepository productionBatchRepository;

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
    // Calculate totalQuantityIssued if not provided
    if (dto.getTotalQuantityIssued() == null && dto.getItems() != null) {
      java.math.BigDecimal sum = java.math.BigDecimal.ZERO;
      for (MaterialIssueDto.Item item : dto.getItems()) {
        if (item.getQuantityIssued() != null) {
          sum = sum.add(item.getQuantityIssued());
        }
      }
      dto.setTotalQuantityIssued(sum);
    }

    MaterialIssue entity;
    if (dto.getId() != null) {
      // Update: load existing entity and update fields
      entity = materialIssueRepository.findById(dto.getId()).orElse(null);
      if (entity == null) {
        // Not found, treat as create
        entity = fromDto(dto);
      } else {
        // Update only fields present in DTO
        if (dto.getProductionBatchId() != null) {
          ProductionBatch pb =
              productionBatchRepository.findById(dto.getProductionBatchId()).orElse(null);
          if (pb != null) {
            entity.setProductionBatch(pb);
          } else {
            // fallback: set by id if not found (should not happen in normal flow)
            ProductionBatch fallbackPb = new ProductionBatch();
            fallbackPb.setId(dto.getProductionBatchId());
            entity.setProductionBatch(fallbackPb);
          }
        }
        if (dto.getSaleId() != null) {
          Sale sale = new Sale();
          sale.setId(dto.getSaleId());
          entity.setSales(sale);
        }
        // Preserve created_at from the existing entity
        entity.setCreatedAt(entity.getCreatedAt());
        entity.setIssueDate(dto.getIssueDate());
        entity.setTotalQuantityIssued(dto.getTotalQuantityIssued());
        entity.setStatus(
            dto.getStatus() != null ? MaterialIssue.Status.valueOf(dto.getStatus()) : null);
        entity.setDepartment(dto.getDepartment());
        entity.setPurpose(dto.getPurpose());
        if (dto.getItems() != null) {
          final MaterialIssue finalEntity = entity;
          entity.setItems(
              dto.getItems().stream()
                  .map(i -> fromItemDto(i, finalEntity))
                  .collect(java.util.stream.Collectors.toList()));
        }
      }
    } else {
      // Create
      entity = fromDto(dto);
    }
    MaterialIssue saved = materialIssueRepository.save(entity);
    return toDto(saved);
  }

  private MaterialIssueDto toDto(MaterialIssue m) {
    MaterialIssueDto dto = new MaterialIssueDto();
    dto.setId(m.getId());
    dto.setProductionBatchId(
        m.getProductionBatch() != null ? m.getProductionBatch().getId() : null);
    dto.setSaleId(m.getSales() != null ? m.getSales().getId() : null);
    dto.setIssueDate(m.getIssueDate());
    dto.setTotalQuantityIssued(m.getTotalQuantityIssued());
    dto.setStatus(m.getStatus() != null ? m.getStatus().name() : null);
    dto.setDepartment(m.getDepartment());
    dto.setPurpose(m.getPurpose());
    if (m.getItems() != null) {
      dto.setItems(m.getItems().stream().map(this::toItemDto).collect(Collectors.toList()));
    }
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
    if (dto.getSaleId() != null) {
      Sale sale = new Sale();
      sale.setId(dto.getSaleId());
      m.setSales(sale);
    }
    m.setIssueDate(dto.getIssueDate());
    m.setTotalQuantityIssued(dto.getTotalQuantityIssued());
    m.setStatus(dto.getStatus() != null ? MaterialIssue.Status.valueOf(dto.getStatus()) : null);
    m.setDepartment(dto.getDepartment());
    m.setPurpose(dto.getPurpose());
    if (dto.getItems() != null) {
      m.setItems(dto.getItems().stream().map(i -> fromItemDto(i, m)).collect(Collectors.toList()));
    }
    return m;
  }

  private MaterialIssueDto.Item toItemDto(MaterialIssueItem item) {
    MaterialIssueDto.Item dto = new MaterialIssueDto.Item();
    dto.setIngredientId(item.getIngredientId());
    dto.setIngredientName(item.getIngredientName());
    dto.setIngredientCode(item.getIngredientCode());
    dto.setQuantityRequired(item.getQuantityRequired());
    dto.setQuantityIssued(item.getQuantityIssued());
    dto.setUnit(item.getUnit());
    dto.setBatchNumber(item.getBatchNumber());
    dto.setExpiryDate(item.getExpiryDate());
    dto.setLotNumber(item.getLotNumber());
    dto.setStorageLocation(item.getStorageLocation());
    dto.setNotes(item.getNotes());
    return dto;
  }

  private MaterialIssueItem fromItemDto(MaterialIssueDto.Item dto, MaterialIssue parent) {
    MaterialIssueItem item = new MaterialIssueItem();
    item.setMaterialIssue(parent);
    item.setIngredientId(dto.getIngredientId());
    item.setIngredientName(dto.getIngredientName());
    item.setIngredientCode(dto.getIngredientCode());
    item.setQuantityRequired(dto.getQuantityRequired());
    item.setQuantityIssued(dto.getQuantityIssued());
    item.setUnit(dto.getUnit());
    item.setBatchNumber(dto.getBatchNumber());
    item.setExpiryDate(dto.getExpiryDate());
    item.setLotNumber(dto.getLotNumber());
    item.setStorageLocation(dto.getStorageLocation());
    item.setNotes(dto.getNotes());
    return item;
  }
}
