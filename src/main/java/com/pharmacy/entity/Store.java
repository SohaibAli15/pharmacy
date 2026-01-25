/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Store {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String code;

  @Column(nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "type_id", nullable = false)
  private StoreTypeEntity type;

  @Column(nullable = false)
  private String address;

  @Column private String city;

  @Column private String state;

  @Column private String country;

  @Column private String zipCode;

  @Column private String phone;

  @Column private String email;

  @ManyToOne
  @JoinColumn(name = "manager_id")
  private User manager;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "status_id", nullable = false)
  private StoreStatusEntity status;

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
}
