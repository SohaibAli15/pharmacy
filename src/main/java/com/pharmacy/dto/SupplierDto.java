/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.time.LocalDateTime;

import com.pharmacy.entity.Supplier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SupplierDto {
  private LocalDateTime updatedAt;
  private LocalDateTime createdAt;
  private Integer paymentTermsDays;
  private String notes;
  private Supplier.SupplierStatus status;
  private String bankAccount;
  private String taxId;
  private String zipCode;
  private String country;
  private String state;
  private String city;
  private String address;
  private String phone;
  private String email;
  private String contactPerson;
  private String code;
  private String name;
  private Long id;
  private Integer totalOrders;
  private java.math.BigDecimal outstandingAmount;
}
