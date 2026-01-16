/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.pharmacy.entity.StockTransfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockTransferDto {
  private Long id;
  private String transferNumber;
  private Long fromStoreId;
  private String fromStoreName;
  private Long toStoreId;
  private String toStoreName;
  private Long requestedById;
  private String requestedByName;
  private Long approvedById;
  private String approvedByName;
  private Long receivedById;
  private String receivedByName;
  private LocalDate transferDate;
  private LocalDate expectedArrivalDate;
  private LocalDate actualArrivalDate;
  private StockTransfer.TransferStatus status;
  private StockTransfer.TransferType type;
  private String notes;
  private String shippingMethod;
  private String trackingNumber;
  private List<StockTransferItemDto> items;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
