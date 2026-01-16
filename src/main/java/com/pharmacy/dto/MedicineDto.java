/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class MedicineDto {
  private Long id;
  private String name;
  private String description;
  private String manufacturer;
  private BigDecimal price;
  private Integer stockQuantity;
  private LocalDate expiryDate;
  private String category;
}
