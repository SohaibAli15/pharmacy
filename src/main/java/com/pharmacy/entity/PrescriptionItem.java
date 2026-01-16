/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prescription_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "prescription_id", nullable = false)
  private Prescription prescription;

  @ManyToOne
  @JoinColumn(name = "medicine_id", nullable = false)
  private Medicine medicine;

  @Column(nullable = false)
  private Integer quantity;

  @Column private String dosageInstructions;
}
