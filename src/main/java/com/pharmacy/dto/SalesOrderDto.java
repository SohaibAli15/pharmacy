/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class SalesOrderDto {
  private Long id;
  private String soNumber;
  private String customer;
  private String product;
  private BigDecimal quantity;
  private BigDecimal dispatched;
  private BigDecimal amount;
  private String workOrder;
  private String status;
  private LocalDate orderDate;
}
