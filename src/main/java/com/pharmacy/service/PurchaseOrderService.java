/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.PurchaseOrderDto;
import com.pharmacy.dto.PurchaseOrderItemDto;
import com.pharmacy.entity.*;
import com.pharmacy.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

  private final PurchaseOrderRepository purchaseOrderRepository;
  private final PurchaseOrderItemRepository purchaseOrderItemRepository;
  private final SupplierRepository supplierRepository;
  private final StoreRepository storeRepository;
  private final UserRepository userRepository;
  private final IngredientRepository ingredientRepository;
  private final IngredientStockRepository ingredientStockRepository;

  @Transactional
  public PurchaseOrderDto createPurchaseOrder(PurchaseOrderDto dto) {
    String orderNumber = generateOrderNumber();

    Supplier supplier =
        supplierRepository
            .findById(dto.getSupplierId())
            .orElseThrow(() -> new RuntimeException("Supplier not found"));
    Store store =
        storeRepository
            .findById(dto.getStoreId())
            .orElseThrow(() -> new RuntimeException("Store not found"));
    User createdBy =
        userRepository
            .findById(dto.getCreatedById())
            .orElseThrow(() -> new RuntimeException("User not found"));

    PurchaseOrder purchaseOrder = new PurchaseOrder();
    purchaseOrder.setOrderNumber(orderNumber);
    purchaseOrder.setSupplier(supplier);
    purchaseOrder.setStore(store);
    purchaseOrder.setCreatedBy(createdBy);
    purchaseOrder.setOrderDate(dto.getOrderDate());
    purchaseOrder.setExpectedDeliveryDate(dto.getExpectedDeliveryDate());
    purchaseOrder.setStatus(PurchaseOrder.OrderStatus.DRAFT);
    purchaseOrder.setSubtotal(dto.getSubtotal());
    purchaseOrder.setTaxAmount(dto.getTaxAmount());
    purchaseOrder.setShippingCost(dto.getShippingCost());
    purchaseOrder.setTotalAmount(dto.getTotalAmount());
    purchaseOrder.setNotes(dto.getNotes());
    purchaseOrder.setShippingAddress(dto.getShippingAddress());
    purchaseOrder.setCreatedAt(LocalDateTime.now());
    purchaseOrder.setUpdatedAt(LocalDateTime.now());

    PurchaseOrder savedOrder = purchaseOrderRepository.save(purchaseOrder);

    // Save items
    if (dto.getItems() != null && !dto.getItems().isEmpty()) {
      for (PurchaseOrderItemDto itemDto : dto.getItems()) {
        Ingredient ingredient =
            ingredientRepository
                .findById(itemDto.getIngredientId())
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        PurchaseOrderItem item = new PurchaseOrderItem();
        item.setPurchaseOrder(savedOrder);
        item.setIngredient(ingredient);
        item.setQuantity(itemDto.getQuantity());
        item.setUnitPrice(itemDto.getUnitPrice());
        item.setTotalPrice(itemDto.getTotalPrice());
        item.setReceivedQuantity(BigDecimal.ZERO);
        item.setStatus(PurchaseOrderItem.ItemStatus.PENDING);
        item.setNotes(itemDto.getNotes());

        purchaseOrderItemRepository.save(item);
      }
    }

    return getPurchaseOrderById(savedOrder.getId());
  }

  @Transactional
  public PurchaseOrderDto updateOrderStatus(Long id, PurchaseOrder.OrderStatus status) {
    PurchaseOrder order =
        purchaseOrderRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Purchase order not found"));

    order.setStatus(status);
    order.setUpdatedAt(LocalDateTime.now());

    if (status == PurchaseOrder.OrderStatus.RECEIVED) {
      order.setActualDeliveryDate(LocalDate.now());
    }

    purchaseOrderRepository.save(order);
    return mapToDto(order);
  }

  @Transactional
  public PurchaseOrderDto receiveItems(Long orderId, List<PurchaseOrderItemDto> receivedItems) {
    PurchaseOrder order =
        purchaseOrderRepository
            .findById(orderId)
            .orElseThrow(() -> new RuntimeException("Purchase order not found"));

    for (PurchaseOrderItemDto receivedItem : receivedItems) {
      PurchaseOrderItem item =
          purchaseOrderItemRepository
              .findById(receivedItem.getId())
              .orElseThrow(() -> new RuntimeException("Item not found"));

      BigDecimal previouslyReceived =
          item.getReceivedQuantity() != null ? item.getReceivedQuantity() : BigDecimal.ZERO;
      BigDecimal newReceived = receivedItem.getReceivedQuantity();
      BigDecimal totalReceived = previouslyReceived.add(newReceived);

      item.setReceivedQuantity(totalReceived);

      if (totalReceived.compareTo(item.getQuantity()) >= 0) {
        item.setStatus(PurchaseOrderItem.ItemStatus.RECEIVED);
      } else if (totalReceived.compareTo(BigDecimal.ZERO) > 0) {
        item.setStatus(PurchaseOrderItem.ItemStatus.PARTIALLY_RECEIVED);
      }

      purchaseOrderItemRepository.save(item);

      // Update ingredient stock
      if (newReceived.compareTo(BigDecimal.ZERO) > 0) {
        updateIngredientStock(order, item, newReceived);
      }
    }

    // Update order status
    updateOrderStatusBasedOnItems(order);
    return mapToDto(order);
  }

  private void updateIngredientStock(
      PurchaseOrder order, PurchaseOrderItem item, BigDecimal receivedQty) {
    String batchNumber = "BATCH-" + System.currentTimeMillis();

    IngredientStock stock = new IngredientStock();
    stock.setStore(order.getStore());
    stock.setIngredient(item.getIngredient());
    stock.setBatchNumber(batchNumber);
    stock.setQuantity(receivedQty);
    stock.setCostPerUnit(item.getUnitPrice());
    stock.setSupplier(order.getSupplier());
    stock.setReceivedDate(LocalDate.now());
    stock.setQualityStatus("APPROVED");
    stock.setCreatedAt(LocalDateTime.now());
    stock.setUpdatedAt(LocalDateTime.now());

    ingredientStockRepository.save(stock);

    // Update ingredient current stock
    Ingredient ingredient = item.getIngredient();
    ingredient.setCurrentStock(ingredient.getCurrentStock().add(receivedQty));
    ingredient.setUpdatedAt(LocalDateTime.now());
    ingredientRepository.save(ingredient);
  }

  private void updateOrderStatusBasedOnItems(PurchaseOrder order) {
    List<PurchaseOrderItem> items = order.getItems();

    boolean allReceived =
        items.stream().allMatch(item -> item.getStatus() == PurchaseOrderItem.ItemStatus.RECEIVED);
    boolean anyReceived =
        items.stream()
            .anyMatch(
                item ->
                    item.getStatus() == PurchaseOrderItem.ItemStatus.RECEIVED
                        || item.getStatus() == PurchaseOrderItem.ItemStatus.PARTIALLY_RECEIVED);

    if (allReceived) {
      order.setStatus(PurchaseOrder.OrderStatus.RECEIVED);
      order.setActualDeliveryDate(LocalDate.now());
    } else if (anyReceived) {
      order.setStatus(PurchaseOrder.OrderStatus.PARTIALLY_RECEIVED);
    }

    order.setUpdatedAt(LocalDateTime.now());
    purchaseOrderRepository.save(order);
  }

  @Transactional(readOnly = true)
  public PurchaseOrderDto getPurchaseOrderById(Long id) {
    PurchaseOrder order =
        purchaseOrderRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Purchase order not found"));
    return mapToDto(order);
  }

  @Transactional(readOnly = true)
  public List<PurchaseOrderDto> getAllPurchaseOrders() {
    return purchaseOrderRepository.findAll().stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<PurchaseOrderDto> getPurchaseOrdersByStatus(PurchaseOrder.OrderStatus status) {
    return purchaseOrderRepository.findByStatus(status).stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<PurchaseOrderDto> getPurchaseOrdersBySupplier(Long supplierId) {
    Supplier supplier =
        supplierRepository
            .findById(supplierId)
            .orElseThrow(() -> new RuntimeException("Supplier not found"));
    return purchaseOrderRepository.findBySupplier(supplier).stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  private String generateOrderNumber() {
    String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    long count = purchaseOrderRepository.count() + 1;
    return "PO-" + datePart + "-" + String.format("%04d", count);
  }

  private PurchaseOrderDto mapToDto(PurchaseOrder order) {
    PurchaseOrderDto dto = new PurchaseOrderDto();
    dto.setId(order.getId());
    dto.setOrderNumber(order.getOrderNumber());
    dto.setSupplierId(order.getSupplier().getId());
    dto.setSupplierName(order.getSupplier().getName());
    dto.setStoreId(order.getStore().getId());
    dto.setStoreName(order.getStore().getName());
    dto.setCreatedById(order.getCreatedBy().getId());
    dto.setCreatedByName(order.getCreatedBy().getUsername());
    dto.setOrderDate(order.getOrderDate());
    dto.setExpectedDeliveryDate(order.getExpectedDeliveryDate());
    dto.setActualDeliveryDate(order.getActualDeliveryDate());
    dto.setStatus(order.getStatus());
    dto.setSubtotal(order.getSubtotal());
    dto.setTaxAmount(order.getTaxAmount());
    dto.setShippingCost(order.getShippingCost());
    dto.setTotalAmount(order.getTotalAmount());
    dto.setNotes(order.getNotes());
    dto.setShippingAddress(order.getShippingAddress());

    if (order.getItems() != null) {
      dto.setItems(order.getItems().stream().map(this::mapItemToDto).collect(Collectors.toList()));
    }

    dto.setCreatedAt(order.getCreatedAt());
    dto.setUpdatedAt(order.getUpdatedAt());
    return dto;
  }

  private PurchaseOrderItemDto mapItemToDto(PurchaseOrderItem item) {
    PurchaseOrderItemDto dto = new PurchaseOrderItemDto();
    dto.setId(item.getId());
    dto.setPurchaseOrderId(item.getPurchaseOrder().getId());
    dto.setIngredientId(item.getIngredient().getId());
    dto.setIngredientName(item.getIngredient().getName());
    dto.setIngredientUnit(item.getIngredient().getUnit());
    dto.setQuantity(item.getQuantity());
    dto.setUnitPrice(item.getUnitPrice());
    dto.setTotalPrice(item.getTotalPrice());
    dto.setReceivedQuantity(item.getReceivedQuantity());
    dto.setNotes(item.getNotes());
    dto.setStatus(item.getStatus());
    return dto;
  }
}
