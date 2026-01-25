/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.AlertDto;
import com.pharmacy.entity.AlertTypeEntity;
import com.pharmacy.repository.AlertTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ThresholdAlertService {

  private final IngredientService ingredientService;
  private final AlertService alertService;
  private final AlertTypeRepository alertTypeRepository;

  // Run every 5 minutes
  @Scheduled(fixedRate = 300000)
  public void checkThresholds() {
    List<com.pharmacy.dto.IngredientDto> lowStock = ingredientService.getLowStockIngredients();
    for (com.pharmacy.dto.IngredientDto ingredient : lowStock) {
      // Check if alert already exists for this ingredient
      List<AlertDto> existingAlerts = alertService.getByIngredient(ingredient.getId());
      boolean alreadyExists =
          existingAlerts.stream()
              .anyMatch(a -> a.getAlertType().equals("LOW_STOCK") && !a.getIsRead());
      if (!alreadyExists) {
        AlertDto alert = new AlertDto();
        alert.setIngredientId(ingredient.getId());
        AlertTypeEntity lowStockType =
            alertTypeRepository
                .findByName("LOW_STOCK")
                .orElseThrow(() -> new RuntimeException("AlertType LOW_STOCK not found"));
        alert.setAlertType(lowStockType.getName());
        alert.setMessage(
            "Low stock alert: "
                + ingredient.getName()
                + " is below threshold ("
                + ingredient.getCurrentStock()
                + " < "
                + ingredient.getThresholdLow()
                + ")");
        alert.setTimestamp(LocalDateTime.now());
        alert.setIsRead(false);
        alertService.create(alert);
        sendNotification(alert);
      }
    }

    List<com.pharmacy.dto.IngredientDto> highStock = ingredientService.getHighStockIngredients();
    for (com.pharmacy.dto.IngredientDto ingredient : highStock) {
      List<AlertDto> existingAlerts = alertService.getByIngredient(ingredient.getId());
      boolean alreadyExists =
          existingAlerts.stream()
              .anyMatch(a -> a.getAlertType().equals("HIGH_STOCK") && !a.getIsRead());
      if (!alreadyExists) {
        AlertDto alert = new AlertDto();
        alert.setIngredientId(ingredient.getId());
        AlertTypeEntity highStockType =
            alertTypeRepository
                .findByName("HIGH_STOCK")
                .orElseThrow(() -> new RuntimeException("AlertType HIGH_STOCK not found"));
        alert.setAlertType(highStockType.getName());
        alert.setMessage(
            "High stock alert: "
                + ingredient.getName()
                + " is above threshold ("
                + ingredient.getCurrentStock()
                + " > "
                + ingredient.getThresholdHigh()
                + ")");
        alert.setTimestamp(LocalDateTime.now());
        alert.setIsRead(false);
        alertService.create(alert);
        sendNotification(alert);
      }
    }
  }

  private void sendNotification(AlertDto alert) {
    // Placeholder for notification logic
    // In a real implementation, integrate with email service, SMS, etc.
    System.out.println("Notification sent: " + alert.getMessage());
    // Example: emailService.sendEmail("admin@pharma.local", "Stock Alert", alert.getMessage());
  }
}
