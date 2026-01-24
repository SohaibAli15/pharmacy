/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.repository.SalesInvoiceRepository;
import com.pharmacy.repository.SalesOrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesDashboardService {
  private final SalesOrderRepository salesOrderRepository;
  private final SalesInvoiceRepository salesInvoiceRepository;

  @Transactional(readOnly = true)
  public Map<String, Object> getSummary() {
    Map<String, Object> summary = new HashMap<>();
    summary.put("totalOrders", salesOrderRepository.count());
    summary.put(
        "inProduction",
        salesOrderRepository.findAll().stream()
            .filter(o -> "IN_PRODUCTION".equals(o.getStatus().toString()))
            .count());
    summary.put(
        "readyToDispatch",
        salesOrderRepository.findAll().stream()
            .filter(o -> "READY_TO_DISPATCH".equals(o.getStatus().toString()))
            .count());
    summary.put(
        "dispatched",
        salesOrderRepository.findAll().stream()
            .filter(o -> "DISPATCHED".equals(o.getStatus().toString()))
            .count());
    summary.put(
        "revenueCollected",
        salesInvoiceRepository.findAll().stream()
            .mapToDouble(i -> i.getAmount() != null ? i.getAmount().doubleValue() : 0)
            .sum());
    summary.put("pendingPayments", 0); // Implement logic if needed
    return summary;
  }
}
