/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.SaleDto;
import com.pharmacy.dto.SaleItemDto;
import com.pharmacy.entity.*;
import com.pharmacy.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleService {

  private final SaleRepository saleRepository;
  private final StoreRepository storeRepository;
  private final CustomerRepository customerRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final InventoryStockRepository inventoryStockRepository;

  @Transactional
  public SaleDto createSale(SaleDto dto) {
    // Validate store
    Store store =
        storeRepository
            .findById(dto.getStoreId())
            .orElseThrow(() -> new RuntimeException("Store not found"));

    // Validate customer (optional for walk-in sales)
    Customer customer = null;
    if (dto.getCustomerId() != null) {
      customer =
          customerRepository
              .findById(dto.getCustomerId())
              .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    // Validate pharmacist
    User pharmacist =
        userRepository
            .findById(dto.getPharmacistId())
            .orElseThrow(() -> new RuntimeException("Pharmacist not found"));

    // Generate invoice number
    String invoiceNumber = generateInvoiceNumber(store);

    // Create sale entity
    Sale sale = new Sale();
    sale.setInvoiceNumber(invoiceNumber);
    sale.setStore(store);
    sale.setCustomer(customer);
    sale.setPharmacist(pharmacist);
    sale.setSaleDate(LocalDateTime.now());
    sale.setSubtotal(dto.getSubtotal());
    sale.setDiscount(dto.getDiscount() != null ? dto.getDiscount() : BigDecimal.ZERO);
    sale.setTaxAmount(dto.getTaxAmount());
    sale.setTotalAmount(dto.getTotalAmount());
    sale.setPaymentMethod(dto.getPaymentMethod());
    sale.setStatus(Sale.SaleStatus.COMPLETED);
    sale.setNotes(dto.getNotes());

    // Save sale first to get ID
    Sale savedSale = saleRepository.save(sale);

    // Process sale items and update inventory
    List<SaleItem> saleItems = new ArrayList<>();
    for (SaleItemDto itemDto : dto.getItems()) {
      Product product =
          productRepository
              .findById(itemDto.getProductId())
              .orElseThrow(
                  () -> new RuntimeException("Product not found: " + itemDto.getProductId()));

      // Check stock availability - find any available stock for this product in the store
      List<InventoryStock> stockList =
          inventoryStockRepository.findByStoreAndProduct(store, product);
      if (stockList.isEmpty()) {
        throw new RuntimeException("Product not available in store: " + product.getName());
      }

      // Calculate total available quantity
      int totalAvailable = stockList.stream().mapToInt(InventoryStock::getQuantity).sum();

      if (totalAvailable < itemDto.getQuantity()) {
        throw new RuntimeException(
            "Insufficient stock for product: "
                + product.getName()
                + ". Available: "
                + totalAvailable
                + ", Required: "
                + itemDto.getQuantity());
      }

      // Create sale item
      SaleItem saleItem = new SaleItem();
      saleItem.setSale(savedSale);
      saleItem.setProduct(product);
      saleItem.setQuantity(itemDto.getQuantity());
      saleItem.setUnitPrice(itemDto.getUnitPrice());
      saleItem.setTotalPrice(itemDto.getTotalPrice());
      saleItems.add(saleItem);

      // Update inventory stock - FIFO approach (use oldest batches first)
      int remainingQty = itemDto.getQuantity();
      for (InventoryStock stock : stockList) {
        if (remainingQty <= 0) break;

        int deductQty = Math.min(stock.getQuantity(), remainingQty);
        stock.setQuantity(stock.getQuantity() - deductQty);
        stock.setUpdatedAt(LocalDateTime.now());
        inventoryStockRepository.save(stock);

        remainingQty -= deductQty;
      }
    }

    savedSale.setItems(saleItems);
    Sale completedSale = saleRepository.save(savedSale);

    return mapToDto(completedSale);
  }

  @Transactional(readOnly = true)
  public SaleDto getSaleById(Long id) {
    Sale sale =
        saleRepository.findById(id).orElseThrow(() -> new RuntimeException("Sale not found"));
    return mapToDto(sale);
  }

  @Transactional(readOnly = true)
  public SaleDto getSaleByInvoiceNumber(String invoiceNumber) {
    Sale sale =
        saleRepository
            .findByInvoiceNumber(invoiceNumber)
            .orElseThrow(
                () -> new RuntimeException("Sale not found with invoice: " + invoiceNumber));
    return mapToDto(sale);
  }

  @Transactional(readOnly = true)
  public Page<SaleDto> getAllSales(Pageable pageable) {
    return saleRepository.findAll(pageable).map(this::mapToDto);
  }

  @Transactional(readOnly = true)
  public Page<SaleDto> getSalesByStore(Long storeId, Pageable pageable) {
    Store store =
        storeRepository
            .findById(storeId)
            .orElseThrow(() -> new RuntimeException("Store not found"));
    return saleRepository.findByStore(store, pageable).map(this::mapToDto);
  }

  @Transactional(readOnly = true)
  public Page<SaleDto> getSalesByCustomer(Long customerId, Pageable pageable) {
    Customer customer =
        customerRepository
            .findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
    return saleRepository.findByCustomer(customer, pageable).map(this::mapToDto);
  }

  @Transactional(readOnly = true)
  public List<SaleDto> getSalesByDateRange(LocalDate startDate, LocalDate endDate) {
    LocalDateTime startDateTime = startDate.atStartOfDay();
    LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
    return saleRepository.findBySaleDateBetween(startDateTime, endDateTime).stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Page<SaleDto> getSalesByStatus(Sale.SaleStatus status, Pageable pageable) {
    return saleRepository.findByStatus(status, pageable).map(this::mapToDto);
  }

  // Backwards compatible list methods
  @Transactional(readOnly = true)
  public List<SaleDto> getAllSales() {
    return getAllSales(org.springframework.data.domain.PageRequest.of(0, 20)).getContent();
  }

  @Transactional(readOnly = true)
  public List<SaleDto> getSalesByStore(Long storeId) {
    return getSalesByStore(storeId, org.springframework.data.domain.PageRequest.of(0, 20))
        .getContent();
  }

  @Transactional(readOnly = true)
  public List<SaleDto> getSalesByCustomer(Long customerId) {
    return getSalesByCustomer(customerId, org.springframework.data.domain.PageRequest.of(0, 20))
        .getContent();
  }

  @Transactional(readOnly = true)
  public List<SaleDto> getSalesByStatus(Sale.SaleStatus status) {
    return getSalesByStatus(status, org.springframework.data.domain.PageRequest.of(0, 20))
        .getContent();
  }

  @Transactional
  public SaleDto cancelSale(Long id) {
    Sale sale =
        saleRepository.findById(id).orElseThrow(() -> new RuntimeException("Sale not found"));

    if (sale.getStatus() == Sale.SaleStatus.CANCELLED) {
      throw new RuntimeException("Sale is already cancelled");
    }

    // Restore inventory stock
    for (SaleItem item : sale.getItems()) {
      List<InventoryStock> stockList =
          inventoryStockRepository.findByStoreAndProduct(sale.getStore(), item.getProduct());
      if (!stockList.isEmpty()) {
        // Add to the first available stock record
        InventoryStock stock = stockList.get(0);
        stock.setQuantity(stock.getQuantity() + item.getQuantity());
        stock.setUpdatedAt(LocalDateTime.now());
        inventoryStockRepository.save(stock);
      }
    }

    sale.setStatus(Sale.SaleStatus.CANCELLED);
    sale.setUpdatedAt(LocalDateTime.now());
    Sale cancelledSale = saleRepository.save(sale);

    return mapToDto(cancelledSale);
  }

  @Transactional
  public SaleDto returnSale(Long id) {
    Sale sale =
        saleRepository.findById(id).orElseThrow(() -> new RuntimeException("Sale not found"));

    if (sale.getStatus() == Sale.SaleStatus.RETURNED) {
      throw new RuntimeException("Sale is already returned");
    }

    // Restore inventory stock
    for (SaleItem item : sale.getItems()) {
      List<InventoryStock> stockList =
          inventoryStockRepository.findByStoreAndProduct(sale.getStore(), item.getProduct());
      if (!stockList.isEmpty()) {
        // Add to the first available stock record
        InventoryStock stock = stockList.get(0);
        stock.setQuantity(stock.getQuantity() + item.getQuantity());
        stock.setUpdatedAt(LocalDateTime.now());
        inventoryStockRepository.save(stock);
      }
    }

    sale.setStatus(Sale.SaleStatus.RETURNED);
    sale.setUpdatedAt(LocalDateTime.now());
    Sale returnedSale = saleRepository.save(sale);

    return mapToDto(returnedSale);
  }

  @Transactional
  public SaleDto updateSaleStatus(Long id, Sale.SaleStatus status) {
    Sale sale =
        saleRepository.findById(id).orElseThrow(() -> new RuntimeException("Sale not found"));
    // Add business rules for valid transitions if needed
    sale.setStatus(status);
    sale.setUpdatedAt(LocalDateTime.now());
    Sale updated = saleRepository.save(sale);
    return mapToDto(updated);
  }

  private String generateInvoiceNumber(Store store) {
    String storeCode = store.getCode();
    String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    long count = saleRepository.count() + 1;
    return String.format("INV-%s-%s-%05d", storeCode, datePart, count);
  }

  private SaleDto mapToDto(Sale sale) {
    SaleDto dto = new SaleDto();
    dto.setId(sale.getId());
    dto.setInvoiceNumber(sale.getInvoiceNumber());
    dto.setStoreId(sale.getStore().getId());
    dto.setStoreName(sale.getStore().getName());

    if (sale.getCustomer() != null) {
      dto.setCustomerId(sale.getCustomer().getId());
      dto.setCustomerName(
          sale.getCustomer().getFirstName() + " " + sale.getCustomer().getLastName());
    }

    dto.setPharmacistId(sale.getPharmacist().getId());
    dto.setPharmacistName(sale.getPharmacist().getUsername());
    dto.setSaleDate(sale.getSaleDate());
    dto.setSubtotal(sale.getSubtotal());
    dto.setDiscount(sale.getDiscount());
    dto.setTaxAmount(sale.getTaxAmount());
    dto.setTotalAmount(sale.getTotalAmount());
    dto.setPaymentMethod(sale.getPaymentMethod());
    dto.setStatus(sale.getStatus());
    dto.setNotes(sale.getNotes());
    dto.setCreatedAt(sale.getCreatedAt());
    dto.setUpdatedAt(sale.getUpdatedAt());

    if (sale.getItems() != null) {
      List<SaleItemDto> itemDtos =
          sale.getItems().stream().map(this::mapItemToDto).collect(Collectors.toList());
      dto.setItems(itemDtos);
    }

    return dto;
  }

  private SaleItemDto mapItemToDto(SaleItem item) {
    SaleItemDto dto = new SaleItemDto();
    dto.setId(item.getId());
    dto.setSaleId(item.getSale().getId());
    dto.setProductId(item.getProduct().getId());
    dto.setProductName(item.getProduct().getName());
    dto.setProductCode("PROD-" + item.getProduct().getId()); // Generate code from ID
    dto.setQuantity(item.getQuantity());
    dto.setUnitPrice(item.getUnitPrice());
    dto.setTotalPrice(item.getTotalPrice());
    return dto;
  }
}
