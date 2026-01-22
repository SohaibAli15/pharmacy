/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
    name = "RecipeStatsResponse",
    description = "Statistics summary for recipes (counts and breakdowns)")
public class RecipeStatsResponse {
  private int totalRecipes;
  private int activeRecipes;
  private int inactiveRecipes;

  @Schema(description = "Breakdown of recipes by category as list of key/count objects")
  private List<KeyCountDto> categoryBreakdown;
}
