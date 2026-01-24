/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.PurchasePaymentDto;
import com.pharmacy.entity.PurchaseInvoice;
import com.pharmacy.entity.PurchasePayment;
import com.pharmacy.repository.PurchaseInvoiceRepository;
import com.pharmacy.repository.PurchasePaymentRepository;
import com.pharmacy.service.PurchasePaymentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchasePaymentServiceImpl implements PurchasePaymentService {
  private final PurchasePaymentRepository paymentRepository;
  private final PurchaseInvoiceRepository invoiceRepository;

  @Override
  public PurchasePaymentDto create(PurchasePaymentDto dto) {
    PurchasePayment payment = toEntity(dto);
    payment.setId(null); // Ensure new entity
    PurchasePayment saved = paymentRepository.save(payment);
    return toDto(saved);
  }

  @Override
  public PurchasePaymentDto update(Long id, PurchasePaymentDto dto) {
    PurchasePayment payment =
        paymentRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + id));
    payment.setDate(dto.getDate());
    payment.setStatus(PurchasePayment.Status.valueOf(dto.getStatus()));
    payment.setReferenceNumber(dto.getReferenceNumber());
    payment.setVendor(dto.getVendor());
    payment.setAmount(dto.getAmount());
    if (dto.getInvoiceId() != null) {
      PurchaseInvoice invoice =
          invoiceRepository
              .findById(dto.getInvoiceId())
              .orElseThrow(
                  () -> new IllegalArgumentException("Invoice not found: " + dto.getInvoiceId()));
      payment.setInvoice(invoice);
    } else {
      payment.setInvoice(null);
    }
    PurchasePayment saved = paymentRepository.save(payment);
    return toDto(saved);
  }

  @Override
  public void delete(Long id) {
    paymentRepository.deleteById(id);
  }

  @Override
  public PurchasePaymentDto getById(Long id) {
    PurchasePayment payment =
        paymentRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + id));
    return toDto(payment);
  }

  @Override
  public List<PurchasePaymentDto> getAll() {
    return paymentRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
  }

  @Override
  public PurchasePaymentDto changeStatus(Long id, String status) {
    PurchasePayment payment =
        paymentRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + id));
    payment.setStatus(PurchasePayment.Status.valueOf(status));
    PurchasePayment saved = paymentRepository.save(payment);
    return toDto(saved);
  }

  private PurchasePaymentDto toDto(PurchasePayment payment) {
    PurchasePaymentDto dto = new PurchasePaymentDto();
    dto.setId(payment.getId());
    dto.setDate(payment.getDate());
    dto.setStatus(payment.getStatus() != null ? payment.getStatus().name() : null);
    dto.setReferenceNumber(payment.getReferenceNumber());
    dto.setVendor(payment.getVendor());
    dto.setAmount(payment.getAmount());
    dto.setInvoiceId(payment.getInvoice() != null ? payment.getInvoice().getId() : null);
    return dto;
  }

  private PurchasePayment toEntity(PurchasePaymentDto dto) {
    PurchasePayment payment = new PurchasePayment();
    payment.setId(dto.getId());
    payment.setDate(dto.getDate());
    payment.setStatus(
        dto.getStatus() != null ? PurchasePayment.Status.valueOf(dto.getStatus()) : null);
    payment.setReferenceNumber(dto.getReferenceNumber());
    payment.setVendor(dto.getVendor());
    payment.setAmount(dto.getAmount());
    if (dto.getInvoiceId() != null) {
      Optional<PurchaseInvoice> invoice = invoiceRepository.findById(dto.getInvoiceId());
      invoice.ifPresent(payment::setInvoice);
    }
    return payment;
  }
}
