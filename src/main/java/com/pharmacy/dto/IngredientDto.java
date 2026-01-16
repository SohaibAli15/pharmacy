/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class IngredientDto {
  private Long id;
  private String name;
  private String description;
  private String unit;
  private BigDecimal currentStock;
  private BigDecimal thresholdLow;
  private BigDecimal thresholdHigh;
  private BigDecimal costPerUnit;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
