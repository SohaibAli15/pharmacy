/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.entity.SalesAgent;
import com.pharmacy.service.SalesAgentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/sales-agents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SalesAgentController {
  private final SalesAgentService salesAgentService;

  @GetMapping
  public ResponseEntity<List<SalesAgent>> getAllAgents() {
    return ResponseEntity.ok(salesAgentService.getAllAgents());
  }

  @GetMapping("/{id}")
  public ResponseEntity<SalesAgent> getAgentById(@PathVariable Long id) {
    return salesAgentService
        .getAgentById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<SalesAgent> createAgent(@RequestBody SalesAgent agent) {
    return ResponseEntity.ok(salesAgentService.createAgent(agent));
  }

  @PutMapping("/{id}")
  public ResponseEntity<SalesAgent> updateAgent(
      @PathVariable Long id, @RequestBody SalesAgent agent) {
    return ResponseEntity.ok(salesAgentService.updateAgent(id, agent));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAgent(@PathVariable Long id) {
    salesAgentService.deleteAgent(id);
    return ResponseEntity.noContent().build();
  }
}
