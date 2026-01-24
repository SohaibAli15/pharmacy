/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;
import java.time.YearMonth;

import lombok.Data;

@Data
public class PayrollDto {
  private Long id;
  private Long employeeId;
  private String employeeName;
  private YearMonth payrollMonth;
  private BigDecimal amount;
  private String status;
}
