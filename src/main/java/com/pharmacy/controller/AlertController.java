/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.AlertDto;
import com.pharmacy.dto.AlertPageResponse;
import com.pharmacy.service.AlertService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
    name = "Alert Management",
    description =
        "APIs for monitoring and managing system alerts (low stock, expiry warnings) (v1)")
public class AlertController {

  private final AlertService alertService;

  @GetMapping
  @Operation(
      summary = "Get all alerts",
      description =
          "Retrieve all system alerts including low stock and expiry warnings with pagination")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved alerts",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = AlertPageResponse.class)))
  public ResponseEntity<AlertPageResponse> getAllAlerts(
      @PageableDefault(size = 20) Pageable pageable) {
    Page<AlertDto> alerts = alertService.listAll(pageable);
    AlertPageResponse response = new AlertPageResponse();
    response.setContent(alerts.getContent());
    response.setTotalElements(alerts.getTotalElements());
    response.setTotalPages(alerts.getTotalPages());
    response.setNumber(alerts.getNumber());
    response.setSize(alerts.getSize());
    response.setFirst(alerts.isFirst());
    response.setLast(alerts.isLast());
    response.setEmpty(alerts.isEmpty());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/unread")
  @Operation(
      summary = "Get unread alerts",
      description = "Retrieve all unread/unacknowledged alerts with pagination")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved unread alerts",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = AlertPageResponse.class)))
  public ResponseEntity<AlertPageResponse> getUnread(
      @PageableDefault(size = 20) Pageable pageable) {
    Page<AlertDto> alerts = alertService.getUnreadAlerts(pageable);
    AlertPageResponse response = new AlertPageResponse();
    response.setContent(alerts.getContent());
    response.setTotalElements(alerts.getTotalElements());
    response.setTotalPages(alerts.getTotalPages());
    response.setNumber(alerts.getNumber());
    response.setSize(alerts.getSize());
    response.setFirst(alerts.isFirst());
    response.setLast(alerts.isLast());
    response.setEmpty(alerts.isEmpty());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/ingredient/{ingredientId}")
  @Operation(
      summary = "Get alerts by ingredient",
      description = "Retrieve alerts related to a specific ingredient with pagination")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved alerts",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AlertPageResponse.class))),
        @ApiResponse(responseCode = "404", description = "Ingredient not found")
      })
  public ResponseEntity<AlertPageResponse> getByIngredient(
      @Parameter(description = "Ingredient ID", required = true) @PathVariable Long ingredientId,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<AlertDto> alerts = alertService.getByIngredient(ingredientId, pageable);
    AlertPageResponse response = new AlertPageResponse();
    response.setContent(alerts.getContent());
    response.setTotalElements(alerts.getTotalElements());
    response.setTotalPages(alerts.getTotalPages());
    response.setNumber(alerts.getNumber());
    response.setSize(alerts.getSize());
    response.setFirst(alerts.isFirst());
    response.setLast(alerts.isLast());
    response.setEmpty(alerts.isEmpty());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get alert by ID", description = "Retrieve a specific alert by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Alert found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AlertDto.class))),
        @ApiResponse(responseCode = "404", description = "Alert not found")
      })
  public ResponseEntity<AlertDto> getAlertById(
      @Parameter(description = "Alert ID", required = true) @PathVariable Long id) {
    AlertDto dto = alertService.getById(id);
    if (dto == null) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(dto);
  }

  @PutMapping("/{id}/read")
  @Operation(
      summary = "Mark alert as read",
      description = "Mark a specific alert as read/acknowledged")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Alert marked as read successfully"),
        @ApiResponse(responseCode = "404", description = "Alert not found")
      })
  public ResponseEntity<Void> markAsRead(
      @Parameter(description = "Alert ID", required = true) @PathVariable Long id) {
    alertService.markAsRead(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete an alert", description = "Remove an alert from the system")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Alert deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Alert not found")
      })
  public ResponseEntity<Void> deleteAlert(
      @Parameter(description = "Alert ID", required = true) @PathVariable Long id) {
    alertService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
