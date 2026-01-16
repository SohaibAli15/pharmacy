/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AlertDto {
  private Long id;
  private Long ingredientId;
  private String ingredientName;
  private String alertType;
  private String message;
  private LocalDateTime timestamp;
  private Boolean isRead;
}
