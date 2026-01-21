/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.InventoryStockDto;
import com.pharmacy.entity.InventoryStock;
import com.pharmacy.entity.Medicine;
import com.pharmacy.entity.Store;
import com.pharmacy.repository.InventoryStockRepository;
import com.pharmacy.repository.MedicineRepository;
import com.pharmacy.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryStockService {

  private final InventoryStockRepository inventoryStockRepository;
  private final StoreRepository storeRepository;
  private final MedicineRepository medicineRepository;

  @Transactional
  public InventoryStockDto addInventoryStock(InventoryStockDto dto) {
    Store store =
        storeRepository
            .findById(dto.getStoreId())
            .orElseThrow(() -> new RuntimeException("Store not found"));
    Medicine medicine =
        medicineRepository
            .findById(dto.getMedicineId())
            .orElseThrow(() -> new RuntimeException("Medicine not found"));

    // Check if batch already exists
    inventoryStockRepository
        .findByStoreAndMedicineAndBatchNumber(store, medicine, dto.getBatchNumber())
        .ifPresent(
            s -> {
              throw new RuntimeException(
                  "Batch number already exists for this medicine in this store");
            });

    InventoryStock stock = new InventoryStock();
    stock.setStore(store);
    stock.setMedicine(medicine);
    stock.setBatchNumber(dto.getBatchNumber());
    stock.setQuantity(dto.getQuantity());
    stock.setCostPrice(dto.getCostPrice());
    stock.setSellingPrice(dto.getSellingPrice());
    stock.setManufacturingDate(dto.getManufacturingDate());
    stock.setExpiryDate(dto.getExpiryDate());
    stock.setReorderLevel(dto.getReorderLevel());
    stock.setMaxStockLevel(dto.getMaxStockLevel());
    stock.setCreatedAt(LocalDateTime.now());
    stock.setUpdatedAt(LocalDateTime.now());

    InventoryStock savedStock = inventoryStockRepository.save(stock);
    return mapToDto(savedStock);
  }

  @Transactional
  public InventoryStockDto updateInventoryStock(Long id, InventoryStockDto dto) {
    InventoryStock stock =
        inventoryStockRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Inventory stock not found"));

    stock.setQuantity(dto.getQuantity());
    stock.setCostPrice(dto.getCostPrice());
    stock.setSellingPrice(dto.getSellingPrice());
    stock.setReorderLevel(dto.getReorderLevel());
    stock.setMaxStockLevel(dto.getMaxStockLevel());
    stock.setUpdatedAt(LocalDateTime.now());

    InventoryStock updatedStock = inventoryStockRepository.save(stock);
    return mapToDto(updatedStock);
  }

  @Transactional
  public void adjustStock(Long id, int quantityChange, String reason) {
    InventoryStock stock =
        inventoryStockRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Inventory stock not found"));

    int newQuantity = stock.getQuantity() + quantityChange;
    if (newQuantity < 0) {
      throw new RuntimeException("Insufficient stock for adjustment");
    }

    stock.setQuantity(newQuantity);
    stock.setUpdatedAt(LocalDateTime.now());
    inventoryStockRepository.save(stock);
  }

  @Transactional(readOnly = true)
  public InventoryStockDto getInventoryStockById(Long id) {
    InventoryStock stock =
        inventoryStockRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Inventory stock not found"));
    return mapToDto(stock);
  }

  @Transactional(readOnly = true)
  public Page<InventoryStockDto> getAllInventoryStock(Pageable pageable) {
    return inventoryStockRepository.findAll(pageable).map(this::mapToDto);
  }

  @Transactional(readOnly = true)
  public Page<InventoryStockDto> getInventoryByStore(Long storeId, Pageable pageable) {
    Store store =
        storeRepository
            .findById(storeId)
            .orElseThrow(() -> new RuntimeException("Store not found"));
    return inventoryStockRepository.findByStore(store, pageable).map(this::mapToDto);
  }

  @Transactional(readOnly = true)
  public Page<InventoryStockDto> getInventoryByMedicine(Long medicineId, Pageable pageable) {
    Medicine medicine =
        medicineRepository
            .findById(medicineId)
            .orElseThrow(() -> new RuntimeException("Medicine not found"));
    return inventoryStockRepository.findByMedicine(medicine, pageable).map(this::mapToDto);
  }

  @Transactional(readOnly = true)
  public Page<InventoryStockDto> getLowStockItems(Long storeId, Pageable pageable) {
    Store store =
        storeRepository
            .findById(storeId)
            .orElseThrow(() -> new RuntimeException("Store not found"));
    return inventoryStockRepository.findLowStockItems(store, pageable).map(this::mapToDto);
  }

  @Transactional(readOnly = true)
  public Page<InventoryStockDto> getExpiringStock(Long storeId, int daysAhead, Pageable pageable) {
    Store store =
        storeRepository
            .findById(storeId)
            .orElseThrow(() -> new RuntimeException("Store not found"));
    LocalDate expiryDate = LocalDate.now().plusDays(daysAhead);
    return inventoryStockRepository
        .findExpiringStock(store, expiryDate, pageable)
        .map(this::mapToDto);
  }

  // Backwards compatible list methods
  @Transactional(readOnly = true)
  public List<InventoryStockDto> getAllInventoryStock() {
    return getAllInventoryStock(org.springframework.data.domain.PageRequest.of(0, 20)).getContent();
  }

  @Transactional(readOnly = true)
  public List<InventoryStockDto> getInventoryByStore(Long storeId) {
    return getInventoryByStore(storeId, org.springframework.data.domain.PageRequest.of(0, 20))
        .getContent();
  }

  @Transactional(readOnly = true)
  public List<InventoryStockDto> getInventoryByMedicine(Long medicineId) {
    return getInventoryByMedicine(medicineId, org.springframework.data.domain.PageRequest.of(0, 20))
        .getContent();
  }

  @Transactional(readOnly = true)
  public List<InventoryStockDto> getLowStockItems(Long storeId) {
    return getLowStockItems(storeId, org.springframework.data.domain.PageRequest.of(0, 20))
        .getContent();
  }

  @Transactional(readOnly = true)
  public List<InventoryStockDto> getExpiringStock(Long storeId, int daysAhead) {
    return getExpiringStock(
            storeId, daysAhead, org.springframework.data.domain.PageRequest.of(0, 20))
        .getContent();
  }

  @Transactional
  public void deleteInventoryStock(Long id) {
    if (!inventoryStockRepository.existsById(id)) {
      throw new RuntimeException("Inventory stock not found");
    }
    inventoryStockRepository.deleteById(id);
  }

  private InventoryStockDto mapToDto(InventoryStock stock) {
    InventoryStockDto dto = new InventoryStockDto();
    dto.setId(stock.getId());
    dto.setStoreId(stock.getStore().getId());
    dto.setStoreName(stock.getStore().getName());
    dto.setMedicineId(stock.getMedicine().getId());
    dto.setMedicineName(stock.getMedicine().getName());
    dto.setBatchNumber(stock.getBatchNumber());
    dto.setQuantity(stock.getQuantity());
    dto.setCostPrice(stock.getCostPrice());
    dto.setSellingPrice(stock.getSellingPrice());
    dto.setManufacturingDate(stock.getManufacturingDate());
    dto.setExpiryDate(stock.getExpiryDate());
    dto.setReorderLevel(stock.getReorderLevel());
    dto.setMaxStockLevel(stock.getMaxStockLevel());
    dto.setCreatedAt(stock.getCreatedAt());
    dto.setUpdatedAt(stock.getUpdatedAt());
    return dto;
  }
}
