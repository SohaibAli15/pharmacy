/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String customerCode;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String phone;

  @Column private String alternatePhone;

  @Column private LocalDate dateOfBirth;

  @Column
  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Column private String address;

  @Column private String city;

  @Column private String state;

  @Column private String country;

  @Column private String zipCode;

  @Column private String insuranceProvider;

  @Column private String insuranceNumber;

  @Column private String allergies;

  @Column private String medicalConditions;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "type_id", nullable = false)
  private CustomerTypeEntity type;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "status_id", nullable = false)
  private CustomerStatusEntity status;

  @Column private String notes;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  public enum Gender {
    MALE,
    FEMALE,
    OTHER
  }
}
