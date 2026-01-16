/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.time.LocalDateTime;

import com.pharmacy.entity.Store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreDto {
  private Long id;
  private String code;
  private String name;
  private Store.StoreType type;
  private String address;
  private String city;
  private String state;
  private String country;
  private String zipCode;
  private String phone;
  private String email;
  private Long managerId;
  private String managerName;
  private Store.StoreStatus status;
  private String notes;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
