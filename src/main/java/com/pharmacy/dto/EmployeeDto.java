/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class EmployeeDto {
  private Long id;
  private String employeeId;
  private String name;
  private String department;
  private String designation;
  private BigDecimal salary;
  private LocalDate joiningDate;
  private String status;
}
