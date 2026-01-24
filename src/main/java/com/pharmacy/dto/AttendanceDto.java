/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AttendanceDto {
  private Long id;
  private Long employeeId;
  private String employeeName;
  private LocalDate date;
  private String status;
}
