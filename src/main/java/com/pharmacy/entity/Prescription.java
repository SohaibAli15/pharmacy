/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prescriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "customer_id", nullable = false)
  private User customer;

  @ManyToOne
  @JoinColumn(name = "pharmacist_id")
  private User pharmacist;

  @Column(nullable = false)
  private LocalDate issueDate;

  @Column(nullable = false)
  private String status; // e.g., PENDING, APPROVED, REJECTED

  @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<PrescriptionItem> items;
}
