/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.util.Map;

import lombok.Data;

@Data
public class ProductionBatchStatsResponse {
  private long totalBatches;
  private long finalizedBatches;
  private long pendingBatches;
  private Map<String, Long> statusBreakdown;
  private Map<String, Long> locationBreakdown;
}
