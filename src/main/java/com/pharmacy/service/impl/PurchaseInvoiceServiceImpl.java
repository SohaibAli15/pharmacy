/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.PurchaseInvoiceDto;
import com.pharmacy.entity.GoodsReceiptNote;
import com.pharmacy.entity.PurchaseInvoice;
import com.pharmacy.repository.GoodsReceiptNoteRepository;
import com.pharmacy.repository.PurchaseInvoiceRepository;
import com.pharmacy.service.PurchaseInvoiceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseInvoiceServiceImpl implements PurchaseInvoiceService {
  private final PurchaseInvoiceRepository invoiceRepository;
  private final GoodsReceiptNoteRepository grnRepository;

  @Override
  public PurchaseInvoiceDto create(PurchaseInvoiceDto dto) {
    PurchaseInvoice invoice = toEntity(dto);
    invoice.setId(null); // Ensure new entity
    PurchaseInvoice saved = invoiceRepository.save(invoice);
    return toDto(saved);
  }

  @Override
  public PurchaseInvoiceDto update(Long id, PurchaseInvoiceDto dto) {
    PurchaseInvoice invoice =
        invoiceRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invoice not found: " + id));
    invoice.setDate(dto.getDate());
    invoice.setStatus(PurchaseInvoice.Status.valueOf(dto.getStatus()));
    invoice.setReferenceNumber(dto.getReferenceNumber());
    invoice.setVendor(dto.getVendor());
    invoice.setAmount(dto.getAmount());
    if (dto.getGrnId() != null) {
      GoodsReceiptNote grn =
          grnRepository
              .findById(dto.getGrnId())
              .orElseThrow(() -> new IllegalArgumentException("GRN not found: " + dto.getGrnId()));
      invoice.setGrn(grn);
    } else {
      invoice.setGrn(null);
    }
    PurchaseInvoice saved = invoiceRepository.save(invoice);
    return toDto(saved);
  }

  @Override
  public void delete(Long id) {
    invoiceRepository.deleteById(id);
  }

  @Override
  public PurchaseInvoiceDto getById(Long id) {
    PurchaseInvoice invoice =
        invoiceRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invoice not found: " + id));
    return toDto(invoice);
  }

  @Override
  public List<PurchaseInvoiceDto> getAll() {
    return invoiceRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
  }

  @Override
  public PurchaseInvoiceDto changeStatus(Long id, String status) {
    PurchaseInvoice invoice =
        invoiceRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invoice not found: " + id));
    invoice.setStatus(PurchaseInvoice.Status.valueOf(status));
    PurchaseInvoice saved = invoiceRepository.save(invoice);
    return toDto(saved);
  }

  private PurchaseInvoiceDto toDto(PurchaseInvoice invoice) {
    PurchaseInvoiceDto dto = new PurchaseInvoiceDto();
    dto.setId(invoice.getId());
    dto.setDate(invoice.getDate());
    dto.setStatus(invoice.getStatus() != null ? invoice.getStatus().name() : null);
    dto.setReferenceNumber(invoice.getReferenceNumber());
    dto.setVendor(invoice.getVendor());
    dto.setAmount(invoice.getAmount());
    dto.setGrnId(invoice.getGrn() != null ? invoice.getGrn().getId() : null);
    return dto;
  }

  private PurchaseInvoice toEntity(PurchaseInvoiceDto dto) {
    PurchaseInvoice invoice = new PurchaseInvoice();
    invoice.setId(dto.getId());
    invoice.setDate(dto.getDate());
    invoice.setStatus(
        dto.getStatus() != null ? PurchaseInvoice.Status.valueOf(dto.getStatus()) : null);
    invoice.setReferenceNumber(dto.getReferenceNumber());
    invoice.setVendor(dto.getVendor());
    invoice.setAmount(dto.getAmount());
    if (dto.getGrnId() != null) {
      Optional<GoodsReceiptNote> grn = grnRepository.findById(dto.getGrnId());
      grn.ifPresent(invoice::setGrn);
    }
    return invoice;
  }
}
