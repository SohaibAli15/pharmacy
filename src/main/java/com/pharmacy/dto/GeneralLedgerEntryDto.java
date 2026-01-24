/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class GeneralLedgerEntryDto {
  private Long id;
  private String entryId;
  private LocalDate date;
  private String account;
  private String description;
  private BigDecimal debit;
  private BigDecimal credit;
  private String type;
  private String company;
}
