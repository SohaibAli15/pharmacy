/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne private Employee employee;

  @Column(nullable = false)
  private LocalDate date;

  @Column(nullable = false)
  private String status; // Present, Absent, Leave, etc.
}
