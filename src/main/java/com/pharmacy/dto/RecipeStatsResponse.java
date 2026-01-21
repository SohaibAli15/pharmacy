package com.pharmacy.dto;

import java.util.Map;

import lombok.Data;

@Data
public class RecipeStatsResponse {
    private int totalRecipes;
    private int activeRecipes;
    private int inactiveRecipes;
    private Map<String, Long> categoryBreakdown;
}
