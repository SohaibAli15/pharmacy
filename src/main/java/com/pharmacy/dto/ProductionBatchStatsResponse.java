/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(
    name = "ProductionBatchStatsResponse",
    description = "Statistics summary for production batches (counts and breakdowns)")
@Data
public class ProductionBatchStatsResponse {
  private long totalBatches;
  private long finalizedBatches;
  private long pendingBatches;

  @Schema(description = "Breakdown of batches by status as list of key/count objects")
  private List<KeyCountDto> statusBreakdown;

  @Schema(description = "Breakdown of batches by business location as list of key/count objects")
  private List<KeyCountDto> locationBreakdown;
}
