/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.pharmacy.entity.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
  private Long id;
  private String customerCode;
  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private String alternatePhone;
  private LocalDate dateOfBirth;
  private Customer.Gender gender;
  private String address;
  private String city;
  private String state;
  private String country;
  private String zipCode;
  private String insuranceProvider;
  private String insuranceNumber;
  private String allergies;
  private String medicalConditions;
  private Customer.CustomerType type;
  private Customer.CustomerStatus status;
  private String notes;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
