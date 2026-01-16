/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionItemDto {
  private Long id;
  private Long prescriptionId;
  private Long medicineId;
  private String medicineName;
  private Integer quantity;
  private String dosageInstructions;
}
