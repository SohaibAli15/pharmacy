/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class LeaveRequestDto {
  private Long id;
  private Long employeeId;
  private String employeeName;
  private String leaveType;
  private LocalDate startDate;
  private LocalDate endDate;
  private String status;
}
