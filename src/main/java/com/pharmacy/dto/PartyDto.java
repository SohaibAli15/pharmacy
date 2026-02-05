/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.time.LocalDateTime;

import com.pharmacy.entity.Party;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyDto {
  private Long id;
  private String partyCode;
  private String partyName;
  private Party.PartyType partyType; // CUSTOMER_ONLY, SUPPLIER_ONLY, BOTH
  private String email;
  private String phone;
  private String address;
  private String city;
  private String state;
  private String country;
  private String zipCode;
  private String taxId;
  private String bankAccount;
  private String notes;
  private Long customerId;
  private Long supplierId;
  private Long accountId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
