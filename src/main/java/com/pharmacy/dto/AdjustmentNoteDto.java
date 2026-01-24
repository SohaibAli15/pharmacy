/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class AdjustmentNoteDto {
  private Long id;
  private String noteId;
  private LocalDate date;
  private String description;
  private BigDecimal amount;
  private String status;
  private String company;
}
