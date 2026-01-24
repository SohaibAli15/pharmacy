/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.SupplierDto;
import com.pharmacy.entity.Supplier;
import com.pharmacy.repository.PurchaseOrderRepository;
import com.pharmacy.repository.SupplierRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierService {

  private final SupplierRepository supplierRepository;
  private final PurchaseOrderRepository purchaseOrderRepository;

  @Transactional
  public SupplierDto createSupplier(SupplierDto supplierDto) {
    if (supplierRepository.existsByCode(supplierDto.getCode())) {
      throw new RuntimeException("Supplier code already exists");
    }

    Supplier supplier = mapToEntity(supplierDto);
    supplier.setCreatedAt(LocalDateTime.now());
    supplier.setUpdatedAt(LocalDateTime.now());

    Supplier savedSupplier = supplierRepository.save(supplier);
    return mapToDto(savedSupplier);
  }

  @Transactional
  public SupplierDto updateSupplier(Long id, SupplierDto supplierDto) {
    Supplier supplier =
        supplierRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Supplier not found"));

    if (!supplier.getCode().equals(supplierDto.getCode())
        && supplierRepository.existsByCode(supplierDto.getCode())) {
      throw new RuntimeException("Supplier code already exists");
    }

    updateEntityFromDto(supplier, supplierDto);
    supplier.setUpdatedAt(LocalDateTime.now());

    Supplier updatedSupplier = supplierRepository.save(supplier);
    return mapToDto(updatedSupplier);
  }

  @Transactional(readOnly = true)
  public SupplierDto getSupplierById(Long id) {
    Supplier supplier =
        supplierRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Supplier not found"));
    return mapToDto(supplier);
  }

  @Transactional(readOnly = true)
  public SupplierDto getSupplierByCode(String code) {
    Supplier supplier =
        supplierRepository
            .findByCode(code)
            .orElseThrow(() -> new RuntimeException("Supplier not found"));
    return mapToDto(supplier);
  }

  @Transactional(readOnly = true)
  public List<SupplierDto> getAllSuppliers() {
    return supplierRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<SupplierDto> getSuppliersByStatus(Supplier.SupplierStatus status) {
    return supplierRepository.findByStatus(status).stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<SupplierDto> searchSuppliersByName(String name) {
    return supplierRepository.findByNameContainingIgnoreCase(name).stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  @Transactional
  public void deleteSupplier(Long id) {
    if (!supplierRepository.existsById(id)) {
      throw new RuntimeException("Supplier not found");
    }
    supplierRepository.deleteById(id);
  }

  private SupplierDto mapToDto(Supplier supplier) {
    SupplierDto dto = new SupplierDto();
    dto.setId(supplier.getId());
    dto.setName(supplier.getName());
    dto.setCode(supplier.getCode());
    dto.setContactPerson(supplier.getContactPerson());
    dto.setEmail(supplier.getEmail());
    dto.setPhone(supplier.getPhone());
    dto.setAddress(supplier.getAddress());
    dto.setCity(supplier.getCity());
    dto.setState(supplier.getState());
    dto.setCountry(supplier.getCountry());
    dto.setZipCode(supplier.getZipCode());
    dto.setTaxId(supplier.getTaxId());
    dto.setBankAccount(supplier.getBankAccount());
    dto.setStatus(supplier.getStatus());
    dto.setNotes(supplier.getNotes());
    dto.setPaymentTermsDays(supplier.getPaymentTermsDays());
    dto.setCreatedAt(supplier.getCreatedAt());
    dto.setUpdatedAt(supplier.getUpdatedAt());
    // Calculate total orders
    int totalOrders =
        purchaseOrderRepository.findBySupplier(supplier, Pageable.unpaged()).getContent().size();
    dto.setTotalOrders(totalOrders);
    // Calculate outstanding amount (sum of totalAmount for orders not RECEIVED or CANCELLED)
    BigDecimal outstanding =
        purchaseOrderRepository.findBySupplier(supplier, Pageable.unpaged()).getContent().stream()
            .filter(
                po ->
                    po.getStatus() != com.pharmacy.entity.PurchaseOrder.OrderStatus.RECEIVED
                        && po.getStatus()
                            != com.pharmacy.entity.PurchaseOrder.OrderStatus.CANCELLED)
            .map(com.pharmacy.entity.PurchaseOrder::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    dto.setOutstandingAmount(outstanding);
    return dto;
  }

  private Supplier mapToEntity(SupplierDto dto) {
    Supplier supplier = new Supplier();
    supplier.setName(dto.getName());
    supplier.setCode(dto.getCode());
    supplier.setContactPerson(dto.getContactPerson());
    supplier.setEmail(dto.getEmail());
    supplier.setPhone(dto.getPhone());
    supplier.setAddress(dto.getAddress());
    supplier.setCity(dto.getCity());
    supplier.setState(dto.getState());
    supplier.setCountry(dto.getCountry());
    supplier.setZipCode(dto.getZipCode());
    supplier.setTaxId(dto.getTaxId());
    supplier.setBankAccount(dto.getBankAccount());
    supplier.setStatus(dto.getStatus());
    supplier.setNotes(dto.getNotes());
    supplier.setPaymentTermsDays(dto.getPaymentTermsDays());
    return supplier;
  }

  private void updateEntityFromDto(Supplier supplier, SupplierDto dto) {
    supplier.setName(dto.getName());
    supplier.setCode(dto.getCode());
    supplier.setContactPerson(dto.getContactPerson());
    supplier.setEmail(dto.getEmail());
    supplier.setPhone(dto.getPhone());
    supplier.setAddress(dto.getAddress());
    supplier.setCity(dto.getCity());
    supplier.setState(dto.getState());
    supplier.setCountry(dto.getCountry());
    supplier.setZipCode(dto.getZipCode());
    supplier.setTaxId(dto.getTaxId());
    supplier.setBankAccount(dto.getBankAccount());
    supplier.setStatus(dto.getStatus());
    supplier.setNotes(dto.getNotes());
    supplier.setPaymentTermsDays(dto.getPaymentTermsDays());
  }
}
