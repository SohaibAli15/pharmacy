/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.StockTransferDto;
import com.pharmacy.dto.StockTransferItemDto;
import com.pharmacy.entity.*;
import com.pharmacy.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockTransferService {

  private final StockTransferRepository stockTransferRepository;
  private final StockTransferItemRepository stockTransferItemRepository;
  private final StoreRepository storeRepository;
  private final UserRepository userRepository;
  private final MedicineRepository medicineRepository;
  private final IngredientRepository ingredientRepository;
  private final InventoryStockRepository inventoryStockRepository;
  private final IngredientStockRepository ingredientStockRepository;

  @Transactional
  public StockTransferDto createStockTransfer(StockTransferDto dto) {
    String transferNumber = generateTransferNumber();

    Store fromStore =
        storeRepository
            .findById(dto.getFromStoreId())
            .orElseThrow(() -> new RuntimeException("Source store not found"));
    Store toStore =
        storeRepository
            .findById(dto.getToStoreId())
            .orElseThrow(() -> new RuntimeException("Destination store not found"));
    User requestedBy =
        userRepository
            .findById(dto.getRequestedById())
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (fromStore.getId().equals(toStore.getId())) {
      throw new RuntimeException("Source and destination stores cannot be the same");
    }

    StockTransfer transfer = new StockTransfer();
    transfer.setTransferNumber(transferNumber);
    transfer.setFromStore(fromStore);
    transfer.setToStore(toStore);
    transfer.setRequestedBy(requestedBy);
    transfer.setTransferDate(dto.getTransferDate());
    transfer.setExpectedArrivalDate(dto.getExpectedArrivalDate());
    transfer.setStatus(StockTransfer.TransferStatus.DRAFT);
    transfer.setType(dto.getType());
    transfer.setNotes(dto.getNotes());
    transfer.setShippingMethod(dto.getShippingMethod());
    transfer.setTrackingNumber(dto.getTrackingNumber());
    transfer.setCreatedAt(LocalDateTime.now());
    transfer.setUpdatedAt(LocalDateTime.now());

    StockTransfer savedTransfer = stockTransferRepository.save(transfer);

    // Save items
    if (dto.getItems() != null && !dto.getItems().isEmpty()) {
      for (StockTransferItemDto itemDto : dto.getItems()) {
        StockTransferItem item = new StockTransferItem();
        item.setStockTransfer(savedTransfer);

        if (itemDto.getMedicineId() != null) {
          Medicine medicine =
              medicineRepository
                  .findById(itemDto.getMedicineId())
                  .orElseThrow(() -> new RuntimeException("Medicine not found"));
          item.setMedicine(medicine);
        }

        if (itemDto.getIngredientId() != null) {
          Ingredient ingredient =
              ingredientRepository
                  .findById(itemDto.getIngredientId())
                  .orElseThrow(() -> new RuntimeException("Ingredient not found"));
          item.setIngredient(ingredient);
        }

        item.setBatchNumber(itemDto.getBatchNumber());
        item.setRequestedQuantity(itemDto.getRequestedQuantity());
        item.setApprovedQuantity(BigDecimal.ZERO);
        item.setReceivedQuantity(BigDecimal.ZERO);
        item.setStatus(StockTransferItem.ItemStatus.PENDING);
        item.setNotes(itemDto.getNotes());

        stockTransferItemRepository.save(item);
      }
    }

    return getStockTransferById(savedTransfer.getId());
  }

  @Transactional
  public StockTransferDto approveStockTransfer(Long id, Long approvedById) {
    StockTransfer transfer =
        stockTransferRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Stock transfer not found"));

    User approvedBy =
        userRepository
            .findById(approvedById)
            .orElseThrow(() -> new RuntimeException("User not found"));

    // Check stock availability
    for (StockTransferItem item : transfer.getItems()) {
      if (item.getMedicine() != null) {
        checkMedicineStock(
            transfer.getFromStore(),
            item.getMedicine(),
            item.getRequestedQuantity(),
            item.getBatchNumber());
      } else if (item.getIngredient() != null) {
        checkIngredientStock(
            transfer.getFromStore(),
            item.getIngredient(),
            item.getRequestedQuantity(),
            item.getBatchNumber());
      }
    }

    transfer.setApprovedBy(approvedBy);
    transfer.setStatus(StockTransfer.TransferStatus.APPROVED);
    transfer.setUpdatedAt(LocalDateTime.now());

    // Approve all items
    for (StockTransferItem item : transfer.getItems()) {
      item.setApprovedQuantity(item.getRequestedQuantity());
      item.setStatus(StockTransferItem.ItemStatus.APPROVED);
      stockTransferItemRepository.save(item);
    }

    stockTransferRepository.save(transfer);
    return mapToDto(transfer);
  }

  @Transactional
  public StockTransferDto dispatchStockTransfer(Long id) {
    StockTransfer transfer =
        stockTransferRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Stock transfer not found"));

    if (transfer.getStatus() != StockTransfer.TransferStatus.APPROVED) {
      throw new RuntimeException("Transfer must be approved before dispatch");
    }

    // Deduct stock from source store
    for (StockTransferItem item : transfer.getItems()) {
      if (item.getMedicine() != null) {
        deductMedicineStock(
            transfer.getFromStore(),
            item.getMedicine(),
            item.getApprovedQuantity(),
            item.getBatchNumber());
      } else if (item.getIngredient() != null) {
        deductIngredientStock(
            transfer.getFromStore(),
            item.getIngredient(),
            item.getApprovedQuantity(),
            item.getBatchNumber());
      }
      item.setStatus(StockTransferItem.ItemStatus.IN_TRANSIT);
      stockTransferItemRepository.save(item);
    }

    transfer.setStatus(StockTransfer.TransferStatus.IN_TRANSIT);
    transfer.setUpdatedAt(LocalDateTime.now());
    stockTransferRepository.save(transfer);

    return mapToDto(transfer);
  }

  @Transactional
  public StockTransferDto receiveStockTransfer(
      Long id, Long receivedById, List<StockTransferItemDto> receivedItems) {
    StockTransfer transfer =
        stockTransferRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Stock transfer not found"));

    User receivedBy =
        userRepository
            .findById(receivedById)
            .orElseThrow(() -> new RuntimeException("User not found"));

    for (StockTransferItemDto receivedItem : receivedItems) {
      StockTransferItem item =
          stockTransferItemRepository
              .findById(receivedItem.getId())
              .orElseThrow(() -> new RuntimeException("Item not found"));

      BigDecimal previouslyReceived =
          item.getReceivedQuantity() != null ? item.getReceivedQuantity() : BigDecimal.ZERO;
      BigDecimal newReceived = receivedItem.getReceivedQuantity();
      BigDecimal totalReceived = previouslyReceived.add(newReceived);

      item.setReceivedQuantity(totalReceived);

      if (totalReceived.compareTo(item.getApprovedQuantity()) >= 0) {
        item.setStatus(StockTransferItem.ItemStatus.RECEIVED);
      } else if (totalReceived.compareTo(BigDecimal.ZERO) > 0) {
        item.setStatus(StockTransferItem.ItemStatus.PARTIALLY_RECEIVED);
      }

      stockTransferItemRepository.save(item);

      // Add stock to destination store
      if (newReceived.compareTo(BigDecimal.ZERO) > 0) {
        if (item.getMedicine() != null) {
          addMedicineStock(transfer.getToStore(), item, newReceived);
        } else if (item.getIngredient() != null) {
          addIngredientStock(transfer.getToStore(), item, newReceived);
        }
      }
    }

    transfer.setReceivedBy(receivedBy);
    transfer.setActualArrivalDate(LocalDate.now());

    // Update transfer status based on items
    updateTransferStatusBasedOnItems(transfer);

    return mapToDto(transfer);
  }

  private void checkMedicineStock(
      Store store, Medicine medicine, BigDecimal requiredQty, String batchNumber) {
    List<InventoryStock> stocks = inventoryStockRepository.findByStoreAndMedicine(store, medicine);

    if (batchNumber != null && !batchNumber.isEmpty()) {
      InventoryStock stock =
          inventoryStockRepository
              .findByStoreAndMedicineAndBatchNumber(store, medicine, batchNumber)
              .orElseThrow(() -> new RuntimeException("Batch not found in source store"));

      if (stock.getQuantity() < requiredQty.intValue()) {
        throw new RuntimeException("Insufficient stock for batch: " + batchNumber);
      }
    } else {
      int totalQty = stocks.stream().mapToInt(InventoryStock::getQuantity).sum();
      if (totalQty < requiredQty.intValue()) {
        throw new RuntimeException("Insufficient stock for medicine: " + medicine.getName());
      }
    }
  }

  private void checkIngredientStock(
      Store store, Ingredient ingredient, BigDecimal requiredQty, String batchNumber) {
    List<IngredientStock> stocks =
        ingredientStockRepository.findByStoreAndIngredient(store, ingredient);

    if (batchNumber != null && !batchNumber.isEmpty()) {
      IngredientStock stock =
          ingredientStockRepository
              .findByStoreAndIngredientAndBatchNumber(store, ingredient, batchNumber)
              .orElseThrow(() -> new RuntimeException("Batch not found in source store"));

      if (stock.getQuantity().compareTo(requiredQty) < 0) {
        throw new RuntimeException("Insufficient stock for batch: " + batchNumber);
      }
    } else {
      Double totalQty =
          ingredientStockRepository.getTotalQuantityByStoreAndIngredient(store, ingredient);
      if (totalQty == null || BigDecimal.valueOf(totalQty).compareTo(requiredQty) < 0) {
        throw new RuntimeException("Insufficient stock for ingredient: " + ingredient.getName());
      }
    }
  }

  private void deductMedicineStock(
      Store store, Medicine medicine, BigDecimal quantity, String batchNumber) {
    if (batchNumber != null && !batchNumber.isEmpty()) {
      InventoryStock stock =
          inventoryStockRepository
              .findByStoreAndMedicineAndBatchNumber(store, medicine, batchNumber)
              .orElseThrow(() -> new RuntimeException("Batch not found"));

      stock.setQuantity(stock.getQuantity() - quantity.intValue());
      stock.setUpdatedAt(LocalDateTime.now());
      inventoryStockRepository.save(stock);
    }
  }

  private void deductIngredientStock(
      Store store, Ingredient ingredient, BigDecimal quantity, String batchNumber) {
    if (batchNumber != null && !batchNumber.isEmpty()) {
      IngredientStock stock =
          ingredientStockRepository
              .findByStoreAndIngredientAndBatchNumber(store, ingredient, batchNumber)
              .orElseThrow(() -> new RuntimeException("Batch not found"));

      stock.setQuantity(stock.getQuantity().subtract(quantity));
      stock.setUpdatedAt(LocalDateTime.now());
      ingredientStockRepository.save(stock);
    }
  }

  private void addMedicineStock(Store store, StockTransferItem item, BigDecimal quantity) {
    InventoryStock existingStock =
        inventoryStockRepository
            .findByStoreAndMedicineAndBatchNumber(store, item.getMedicine(), item.getBatchNumber())
            .orElse(null);

    if (existingStock != null) {
      existingStock.setQuantity(existingStock.getQuantity() + quantity.intValue());
      existingStock.setUpdatedAt(LocalDateTime.now());
      inventoryStockRepository.save(existingStock);
    } else {
      InventoryStock newStock = new InventoryStock();
      newStock.setStore(store);
      newStock.setMedicine(item.getMedicine());
      newStock.setBatchNumber(item.getBatchNumber());
      newStock.setQuantity(quantity.intValue());
      newStock.setCostPrice(BigDecimal.ZERO);
      newStock.setSellingPrice(item.getMedicine().getPrice());
      newStock.setExpiryDate(item.getMedicine().getExpiryDate());
      newStock.setCreatedAt(LocalDateTime.now());
      newStock.setUpdatedAt(LocalDateTime.now());
      inventoryStockRepository.save(newStock);
    }
  }

  private void addIngredientStock(Store store, StockTransferItem item, BigDecimal quantity) {
    IngredientStock existingStock =
        ingredientStockRepository
            .findByStoreAndIngredientAndBatchNumber(
                store, item.getIngredient(), item.getBatchNumber())
            .orElse(null);

    if (existingStock != null) {
      existingStock.setQuantity(existingStock.getQuantity().add(quantity));
      existingStock.setUpdatedAt(LocalDateTime.now());
      ingredientStockRepository.save(existingStock);
    } else {
      IngredientStock newStock = new IngredientStock();
      newStock.setStore(store);
      newStock.setIngredient(item.getIngredient());
      newStock.setBatchNumber(item.getBatchNumber());
      newStock.setQuantity(quantity);
      newStock.setCostPerUnit(item.getIngredient().getCostPerUnit());
      newStock.setReceivedDate(LocalDate.now());
      newStock.setQualityStatus("APPROVED");
      newStock.setCreatedAt(LocalDateTime.now());
      newStock.setUpdatedAt(LocalDateTime.now());
      ingredientStockRepository.save(newStock);
    }
  }

  private void updateTransferStatusBasedOnItems(StockTransfer transfer) {
    List<StockTransferItem> items = transfer.getItems();

    boolean allReceived =
        items.stream().allMatch(item -> item.getStatus() == StockTransferItem.ItemStatus.RECEIVED);
    boolean anyReceived =
        items.stream()
            .anyMatch(
                item ->
                    item.getStatus() == StockTransferItem.ItemStatus.RECEIVED
                        || item.getStatus() == StockTransferItem.ItemStatus.PARTIALLY_RECEIVED);

    if (allReceived) {
      transfer.setStatus(StockTransfer.TransferStatus.RECEIVED);
    } else if (anyReceived) {
      transfer.setStatus(StockTransfer.TransferStatus.PARTIALLY_RECEIVED);
    }

    transfer.setUpdatedAt(LocalDateTime.now());
    stockTransferRepository.save(transfer);
  }

  @Transactional(readOnly = true)
  public StockTransferDto getStockTransferById(Long id) {
    StockTransfer transfer =
        stockTransferRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Stock transfer not found"));
    return mapToDto(transfer);
  }

  @Transactional(readOnly = true)
  public Page<StockTransferDto> getAllStockTransfers(Pageable pageable) {
    return stockTransferRepository.findAll(pageable).map(this::mapToDto);
  }

  @Transactional(readOnly = true)
  public Page<StockTransferDto> getStockTransfersByStore(Long storeId, Pageable pageable) {
    Store store =
        storeRepository
            .findById(storeId)
            .orElseThrow(() -> new RuntimeException("Store not found"));
    return stockTransferRepository
        .findByFromStoreOrToStore(store, store, pageable)
        .map(this::mapToDto);
  }

  // Backwards compatible list methods
  @Transactional(readOnly = true)
  public List<StockTransferDto> getAllStockTransfers() {
    return getAllStockTransfers(org.springframework.data.domain.PageRequest.of(0, 20)).getContent();
  }

  @Transactional(readOnly = true)
  public List<StockTransferDto> getStockTransfersByStore(Long storeId) {
    return getStockTransfersByStore(storeId, org.springframework.data.domain.PageRequest.of(0, 20))
        .getContent();
  }

  private String generateTransferNumber() {
    String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    long count = stockTransferRepository.count() + 1;
    return "ST-" + datePart + "-" + String.format("%04d", count);
  }

  private StockTransferDto mapToDto(StockTransfer transfer) {
    StockTransferDto dto = new StockTransferDto();
    dto.setId(transfer.getId());
    dto.setTransferNumber(transfer.getTransferNumber());
    dto.setFromStoreId(transfer.getFromStore().getId());
    dto.setFromStoreName(transfer.getFromStore().getName());
    dto.setToStoreId(transfer.getToStore().getId());
    dto.setToStoreName(transfer.getToStore().getName());
    dto.setRequestedById(transfer.getRequestedBy().getId());
    dto.setRequestedByName(transfer.getRequestedBy().getUsername());

    if (transfer.getApprovedBy() != null) {
      dto.setApprovedById(transfer.getApprovedBy().getId());
      dto.setApprovedByName(transfer.getApprovedBy().getUsername());
    }

    if (transfer.getReceivedBy() != null) {
      dto.setReceivedById(transfer.getReceivedBy().getId());
      dto.setReceivedByName(transfer.getReceivedBy().getUsername());
    }

    dto.setTransferDate(transfer.getTransferDate());
    dto.setExpectedArrivalDate(transfer.getExpectedArrivalDate());
    dto.setActualArrivalDate(transfer.getActualArrivalDate());
    dto.setStatus(transfer.getStatus());
    dto.setType(transfer.getType());
    dto.setNotes(transfer.getNotes());
    dto.setShippingMethod(transfer.getShippingMethod());
    dto.setTrackingNumber(transfer.getTrackingNumber());

    if (transfer.getItems() != null) {
      dto.setItems(
          transfer.getItems().stream().map(this::mapItemToDto).collect(Collectors.toList()));
    }

    dto.setCreatedAt(transfer.getCreatedAt());
    dto.setUpdatedAt(transfer.getUpdatedAt());
    return dto;
  }

  private StockTransferItemDto mapItemToDto(StockTransferItem item) {
    StockTransferItemDto dto = new StockTransferItemDto();
    dto.setId(item.getId());
    dto.setStockTransferId(item.getStockTransfer().getId());

    if (item.getMedicine() != null) {
      dto.setMedicineId(item.getMedicine().getId());
      dto.setMedicineName(item.getMedicine().getName());
    }

    if (item.getIngredient() != null) {
      dto.setIngredientId(item.getIngredient().getId());
      dto.setIngredientName(item.getIngredient().getName());
    }

    dto.setBatchNumber(item.getBatchNumber());
    dto.setRequestedQuantity(item.getRequestedQuantity());
    dto.setApprovedQuantity(item.getApprovedQuantity());
    dto.setReceivedQuantity(item.getReceivedQuantity());
    dto.setNotes(item.getNotes());
    dto.setStatus(item.getStatus());
    return dto;
  }
}
