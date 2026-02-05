/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Party entity represents any business entity (customer, supplier, or both) A single party can be
 * both a customer and supplier, sharing one unified ledger account
 */
@Entity
@Table(name = "parties")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Party {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String partyCode;

  @Column(nullable = false)
  private String partyName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PartyType partyType; // CUSTOMER_ONLY, SUPPLIER_ONLY, BOTH

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String phone;

  @Column private String address;

  @Column private String city;

  @Column private String state;

  @Column private String country;

  @Column private String zipCode;

  @Column private String taxId;

  @Column private String bankAccount;

  @Column private String notes;

  // Foreign keys - nullable because a party might only be a customer or only a supplier
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", unique = true)
  private Customer customer;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "supplier_id", unique = true)
  private Supplier supplier;

  // One party has one unified ledger account
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "account_id", unique = true)
  private Account account;

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

  public enum PartyType {
    CUSTOMER_ONLY,
    SUPPLIER_ONLY,
    BOTH
  }
}
