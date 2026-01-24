/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.GoodsReceiptNoteDto;
import com.pharmacy.entity.GoodsReceiptNote;
import com.pharmacy.entity.PurchaseOrder;
import com.pharmacy.repository.GoodsReceiptNoteRepository;
import com.pharmacy.repository.PurchaseOrderRepository;
import com.pharmacy.service.GoodsReceiptNoteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsReceiptNoteServiceImpl implements GoodsReceiptNoteService {
  private final GoodsReceiptNoteRepository grnRepository;
  private final PurchaseOrderRepository purchaseOrderRepository;

  @Override
  public GoodsReceiptNoteDto create(GoodsReceiptNoteDto dto) {
    GoodsReceiptNote grn = toEntity(dto);
    grn.setId(null); // Ensure new entity
    GoodsReceiptNote saved = grnRepository.save(grn);
    return toDto(saved);
  }

  @Override
  public GoodsReceiptNoteDto update(Long id, GoodsReceiptNoteDto dto) {
    GoodsReceiptNote grn =
        grnRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("GRN not found: " + id));
    // Update fields
    grn.setDate(dto.getDate());
    grn.setStatus(GoodsReceiptNote.Status.valueOf(dto.getStatus()));
    grn.setReferenceNumber(dto.getReferenceNumber());
    grn.setVendor(dto.getVendor());
    grn.setAmount(dto.getAmount());
    if (dto.getPurchaseOrderId() != null) {
      PurchaseOrder po =
          purchaseOrderRepository
              .findById(dto.getPurchaseOrderId())
              .orElseThrow(
                  () ->
                      new IllegalArgumentException(
                          "PurchaseOrder not found: " + dto.getPurchaseOrderId()));
      grn.setPurchaseOrder(po);
    } else {
      grn.setPurchaseOrder(null);
    }
    GoodsReceiptNote saved = grnRepository.save(grn);
    return toDto(saved);
  }

  @Override
  public void delete(Long id) {
    grnRepository.deleteById(id);
  }

  @Override
  public GoodsReceiptNoteDto getById(Long id) {
    GoodsReceiptNote grn =
        grnRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("GRN not found: " + id));
    return toDto(grn);
  }

  @Override
  public List<GoodsReceiptNoteDto> getAll() {
    return grnRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
  }

  @Override
  public GoodsReceiptNoteDto changeStatus(Long id, String status) {
    GoodsReceiptNote grn =
        grnRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("GRN not found: " + id));
    grn.setStatus(GoodsReceiptNote.Status.valueOf(status));
    GoodsReceiptNote saved = grnRepository.save(grn);
    return toDto(saved);
  }

  private GoodsReceiptNoteDto toDto(GoodsReceiptNote grn) {
    GoodsReceiptNoteDto dto = new GoodsReceiptNoteDto();
    dto.setId(grn.getId());
    dto.setDate(grn.getDate());
    dto.setStatus(grn.getStatus() != null ? grn.getStatus().name() : null);
    dto.setReferenceNumber(grn.getReferenceNumber());
    dto.setVendor(grn.getVendor());
    dto.setAmount(grn.getAmount());
    dto.setPurchaseOrderId(grn.getPurchaseOrder() != null ? grn.getPurchaseOrder().getId() : null);
    return dto;
  }

  private GoodsReceiptNote toEntity(GoodsReceiptNoteDto dto) {
    GoodsReceiptNote grn = new GoodsReceiptNote();
    grn.setId(dto.getId());
    grn.setDate(dto.getDate());
    grn.setStatus(
        dto.getStatus() != null ? GoodsReceiptNote.Status.valueOf(dto.getStatus()) : null);
    grn.setReferenceNumber(dto.getReferenceNumber());
    grn.setVendor(dto.getVendor());
    grn.setAmount(dto.getAmount());
    if (dto.getPurchaseOrderId() != null) {
      Optional<PurchaseOrder> po = purchaseOrderRepository.findById(dto.getPurchaseOrderId());
      po.ifPresent(grn::setPurchaseOrder);
    }
    return grn;
  }
}
