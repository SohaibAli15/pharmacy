/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import lombok.Data;

@Data
public class IssueMaterialDto {
  private Long stockId;
  private Integer quantity;
  private String reason;
}
