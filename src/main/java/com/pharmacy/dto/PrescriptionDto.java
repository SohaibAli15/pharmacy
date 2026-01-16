/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDto {
  private Long id;
  private Long customerId;
  private String customerName;
  private Long pharmacistId;
  private String pharmacistName;
  private LocalDate issueDate;
  private String status;
  private List<PrescriptionItemDto> items;
}
