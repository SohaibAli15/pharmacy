/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.SalesInvoiceDto;
import com.pharmacy.entity.SalesInvoice;
import com.pharmacy.entity.SalesOrder;
import com.pharmacy.repository.SalesInvoiceRepository;
import com.pharmacy.repository.SalesOrderRepository;
import com.pharmacy.service.SalesInvoiceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesInvoiceServiceImpl implements SalesInvoiceService {
  private final SalesInvoiceRepository invoiceRepository;
  private final SalesOrderRepository orderRepository;

  @Override
  public SalesInvoiceDto create(SalesInvoiceDto dto) {
    SalesInvoice invoice = toEntity(dto);
    invoice.setId(null); // Ensure new entity
    SalesInvoice saved = invoiceRepository.save(invoice);
    return toDto(saved);
  }

  @Override
  public SalesInvoiceDto update(Long id, SalesInvoiceDto dto) {
    SalesInvoice invoice =
        invoiceRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invoice not found: " + id));
    if (dto.getSalesOrderId() != null) {
      SalesOrder order =
          orderRepository
              .findById(dto.getSalesOrderId())
              .orElseThrow(
                  () ->
                      new IllegalArgumentException(
                          "SalesOrder not found: " + dto.getSalesOrderId()));
      invoice.setSalesOrder(order);
    } else {
      invoice.setSalesOrder(null);
    }
    invoice.setInvoiceNumber(dto.getInvoiceNumber());
    invoice.setInvoiceDate(dto.getInvoiceDate());
    invoice.setAmount(dto.getAmount());
    invoice.setStatus(dto.getStatus());
    SalesInvoice saved = invoiceRepository.save(invoice);
    return toDto(saved);
  }

  @Override
  public void delete(Long id) {
    invoiceRepository.deleteById(id);
  }

  @Override
  public SalesInvoiceDto getById(Long id) {
    SalesInvoice invoice =
        invoiceRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invoice not found: " + id));
    return toDto(invoice);
  }

  @Override
  public List<SalesInvoiceDto> getAll() {
    return invoiceRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
  }

  @Override
  public SalesInvoiceDto changeStatus(Long id, String status) {
    SalesInvoice invoice =
        invoiceRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invoice not found: " + id));
    invoice.setStatus(status);
    SalesInvoice saved = invoiceRepository.save(invoice);
    return toDto(saved);
  }

  private SalesInvoiceDto toDto(SalesInvoice invoice) {
    SalesInvoiceDto dto = new SalesInvoiceDto();
    dto.setId(invoice.getId());
    dto.setSalesOrderId(invoice.getSalesOrder() != null ? invoice.getSalesOrder().getId() : null);
    dto.setInvoiceNumber(invoice.getInvoiceNumber());
    dto.setInvoiceDate(invoice.getInvoiceDate());
    dto.setAmount(invoice.getAmount());
    dto.setStatus(invoice.getStatus());
    return dto;
  }

  private SalesInvoice toEntity(SalesInvoiceDto dto) {
    SalesInvoice invoice = new SalesInvoice();
    invoice.setId(dto.getId());
    if (dto.getSalesOrderId() != null) {
      Optional<SalesOrder> order = orderRepository.findById(dto.getSalesOrderId());
      order.ifPresent(invoice::setSalesOrder);
    }
    invoice.setInvoiceNumber(dto.getInvoiceNumber());
    invoice.setInvoiceDate(dto.getInvoiceDate());
    invoice.setAmount(dto.getAmount());
    invoice.setStatus(dto.getStatus());
    return invoice;
  }
}
