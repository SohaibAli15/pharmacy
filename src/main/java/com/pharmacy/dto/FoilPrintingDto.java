/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FoilPrintingDto {
  private Long id;
  private String jobNumber;
  private String materialCode;
  private String foilType;
  private Integer sheetsUsed;
  private Integer bundlesProduced;
  private BigDecimal cost;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
