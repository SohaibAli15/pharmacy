/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "suppliers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String code;

  @Column(nullable = false)
  private String contactPerson;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String phone;

  @Column(nullable = false)
  private String address;

  @Column private String city;

  @Column private String state;

  @Column private String country;

  @Column private String zipCode;

  @Column private String taxId;

  @Column private String bankAccount;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "status_id", nullable = false)
  private SupplierStatusEntity status;

  @Column private String notes;

  @Column private Integer paymentTermsDays; // e.g., Net 30, Net 60

  // Unified party relationship - one supplier linked to one party
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "party_id", unique = true)
  private Party party;

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
}
