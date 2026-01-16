/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.AlertDto;
import com.pharmacy.entity.Alert;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ThresholdAlertService {

  private final IngredientService ingredientService;
  private final AlertService alertService;

  // Run every 5 minutes
  @Scheduled(fixedRate = 300000)
  public void checkThresholds() {
    List<com.pharmacy.dto.IngredientDto> lowStock = ingredientService.getLowStockIngredients();
    for (com.pharmacy.dto.IngredientDto ingredient : lowStock) {
      // Check if alert already exists for this ingredient
      List<AlertDto> existingAlerts = alertService.getByIngredient(ingredient.getId());
      boolean alreadyExists =
          existingAlerts.stream()
              .anyMatch(
                  a -> a.getAlertType().equals(Alert.AlertType.LOW_STOCK.name()) && !a.getIsRead());
      if (!alreadyExists) {
        AlertDto alert = new AlertDto();
        alert.setIngredientId(ingredient.getId());
        alert.setAlertType(Alert.AlertType.LOW_STOCK.name());
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
        // Here you can add notification logic, e.g., email, SMS
        sendNotification(alert);
      }
    }

    List<com.pharmacy.dto.IngredientDto> highStock = ingredientService.getHighStockIngredients();
    for (com.pharmacy.dto.IngredientDto ingredient : highStock) {
      List<AlertDto> existingAlerts = alertService.getByIngredient(ingredient.getId());
      boolean alreadyExists =
          existingAlerts.stream()
              .anyMatch(
                  a ->
                      a.getAlertType().equals(Alert.AlertType.HIGH_STOCK.name()) && !a.getIsRead());
      if (!alreadyExists) {
        AlertDto alert = new AlertDto();
        alert.setIngredientId(ingredient.getId());
        alert.setAlertType(Alert.AlertType.HIGH_STOCK.name());
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
