/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.IngredientStockDto;
import com.pharmacy.entity.Ingredient;
import com.pharmacy.entity.IngredientStock;
import com.pharmacy.entity.Store;
import com.pharmacy.entity.Supplier;
import com.pharmacy.repository.IngredientRepository;
import com.pharmacy.repository.IngredientStockRepository;
import com.pharmacy.repository.StoreRepository;
import com.pharmacy.repository.SupplierRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IngredientStockService {

  private final IngredientStockRepository ingredientStockRepository;
  private final StoreRepository storeRepository;
  private final IngredientRepository ingredientRepository;
  private final SupplierRepository supplierRepository;

  @Transactional
  public IngredientStockDto addIngredientStock(IngredientStockDto dto) {
    Store store =
        storeRepository
            .findById(dto.getStoreId())
            .orElseThrow(() -> new RuntimeException("Store not found"));
    Ingredient ingredient =
        ingredientRepository
            .findById(dto.getIngredientId())
            .orElseThrow(() -> new RuntimeException("Ingredient not found"));

    // Check if batch already exists
    ingredientStockRepository
        .findByStoreAndIngredientAndBatchNumber(store, ingredient, dto.getBatchNumber())
        .ifPresent(
            s -> {
              throw new RuntimeException(
                  "Batch number already exists for this ingredient in this store");
            });

    IngredientStock stock = new IngredientStock();
    stock.setStore(store);
    stock.setIngredient(ingredient);
    stock.setBatchNumber(dto.getBatchNumber());
    stock.setQuantity(dto.getQuantity());
    stock.setCostPerUnit(dto.getCostPerUnit());

    if (dto.getSupplierId() != null) {
      Supplier supplier =
          supplierRepository
              .findById(dto.getSupplierId())
              .orElseThrow(() -> new RuntimeException("Supplier not found"));
      stock.setSupplier(supplier);
    }

    stock.setReceivedDate(dto.getReceivedDate());
    stock.setExpiryDate(dto.getExpiryDate());
    stock.setQualityStatus(dto.getQualityStatus());
    stock.setCreatedAt(LocalDateTime.now());
    stock.setUpdatedAt(LocalDateTime.now());

    IngredientStock savedStock = ingredientStockRepository.save(stock);
    return mapToDto(savedStock);
  }

  @Transactional
  public IngredientStockDto updateIngredientStock(Long id, IngredientStockDto dto) {
    IngredientStock stock =
        ingredientStockRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Ingredient stock not found"));

    stock.setQuantity(dto.getQuantity());
    stock.setCostPerUnit(dto.getCostPerUnit());
    stock.setQualityStatus(dto.getQualityStatus());
    stock.setUpdatedAt(LocalDateTime.now());

    IngredientStock updatedStock = ingredientStockRepository.save(stock);
    return mapToDto(updatedStock);
  }

  @Transactional
  public void adjustStock(Long id, BigDecimal quantityChange, String reason) {
    IngredientStock stock =
        ingredientStockRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Ingredient stock not found"));

    BigDecimal newQuantity = stock.getQuantity().add(quantityChange);
    if (newQuantity.compareTo(BigDecimal.ZERO) < 0) {
      throw new RuntimeException("Insufficient stock for adjustment");
    }

    stock.setQuantity(newQuantity);
    stock.setUpdatedAt(LocalDateTime.now());
    ingredientStockRepository.save(stock);
  }

  @Transactional(readOnly = true)
  public IngredientStockDto getIngredientStockById(Long id) {
    IngredientStock stock =
        ingredientStockRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Ingredient stock not found"));
    return mapToDto(stock);
  }

  @Transactional(readOnly = true)
  public Page<IngredientStockDto> getAllIngredientStock(Pageable pageable) {
    return ingredientStockRepository.findAll(pageable).map(this::mapToDto);
  }

  @Transactional(readOnly = true)
  public Page<IngredientStockDto> getIngredientStockByStore(Long storeId, Pageable pageable) {
    Store store =
        storeRepository
            .findById(storeId)
            .orElseThrow(() -> new RuntimeException("Store not found"));
    return ingredientStockRepository.findByStore(store, pageable).map(this::mapToDto);
  }

  @Transactional(readOnly = true)
  public Page<IngredientStockDto> getIngredientStockByIngredient(
      Long ingredientId, Pageable pageable) {
    Ingredient ingredient =
        ingredientRepository
            .findById(ingredientId)
            .orElseThrow(() -> new RuntimeException("Ingredient not found"));
    return ingredientStockRepository.findByIngredient(ingredient, pageable).map(this::mapToDto);
  }

  @Transactional(readOnly = true)
  public Double getTotalQuantity(Long storeId, Long ingredientId) {
    Store store =
        storeRepository
            .findById(storeId)
            .orElseThrow(() -> new RuntimeException("Store not found"));
    Ingredient ingredient =
        ingredientRepository
            .findById(ingredientId)
            .orElseThrow(() -> new RuntimeException("Ingredient not found"));

    return ingredientStockRepository.getTotalQuantityByStoreAndIngredient(store, ingredient);
  }

  @Transactional
  public void deleteIngredientStock(Long id) {
    if (!ingredientStockRepository.existsById(id)) {
      throw new RuntimeException("Ingredient stock not found");
    }
    ingredientStockRepository.deleteById(id);
  }

  private IngredientStockDto mapToDto(IngredientStock stock) {
    IngredientStockDto dto = new IngredientStockDto();
    dto.setId(stock.getId());
    dto.setStoreId(stock.getStore().getId());
    dto.setStoreName(stock.getStore().getName());
    dto.setIngredientId(stock.getIngredient().getId());
    dto.setIngredientName(stock.getIngredient().getName());
    dto.setIngredientUnit(stock.getIngredient().getUnit());
    dto.setBatchNumber(stock.getBatchNumber());
    dto.setQuantity(stock.getQuantity());
    dto.setCostPerUnit(stock.getCostPerUnit());

    if (stock.getSupplier() != null) {
      dto.setSupplierId(stock.getSupplier().getId());
      dto.setSupplierName(stock.getSupplier().getName());
    }

    dto.setReceivedDate(stock.getReceivedDate());
    dto.setExpiryDate(stock.getExpiryDate());
    dto.setQualityStatus(stock.getQualityStatus());
    dto.setCreatedAt(stock.getCreatedAt());
    dto.setUpdatedAt(stock.getUpdatedAt());
    return dto;
  }
}
