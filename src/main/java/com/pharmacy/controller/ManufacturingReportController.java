package com.pharmacy.controller;

import com.pharmacy.dto.ProductionBatchDto;
import com.pharmacy.service.ProductionBatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manufacturing Report Controller
 * Provides comprehensive reporting endpoints for manufacturing operations
 * Following pharmaceutical GMP compliance requirements
 */
@RestController
@RequestMapping("/api/v1/manufacturing-reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Manufacturing Reports", description = "Analytics and reporting endpoints for manufacturing operations and GMP compliance (v1)")
public class ManufacturingReportController {

    private final ProductionBatchService productionBatchService;

    @Operation(
        summary = "Material Consumption Report",
        description = """
            Comprehensive material consumption analysis across production batches.
            Shows total material usage, variances, and costs with filtering options.
            Useful for inventory management and cost control.
            """
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully generated material consumption report",
            content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/material-consumption")
    public ResponseEntity<Map<String, Object>> getMaterialConsumptionReport(
            @Parameter(description = "Start date for report (ISO 8601 format)", example = "2026-01-01T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date for report (ISO 8601 format)", example = "2026-12-31T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Filter by business location", example = "Butt Brothers")
            @RequestParam(required = false) String businessLocation,
            @Parameter(description = "Filter by specific ingredient ID")
            @RequestParam(required = false) Long ingredientId
    ) {
        List<ProductionBatchDto> batches = productionBatchService.searchProductionBatches(
            businessLocation, null, null, null, null, startDate, endDate
        );

        // Aggregate material consumption
        Map<Long, Map<String, Object>> materialConsumption = new HashMap<>();

        batches.forEach(batch -> {
            if (batch.getMaterialsConsumed() != null) {
                batch.getMaterialsConsumed().forEach(material -> {
                    if (ingredientId == null || material.getIngredientId().equals(ingredientId)) {
                        Long ingId = material.getIngredientId();

                        materialConsumption.putIfAbsent(ingId, new HashMap<>());
                        Map<String, Object> ingData = materialConsumption.get(ingId);

                        ingData.put("ingredientId", material.getIngredientId());
                        ingData.put("ingredientName", material.getIngredientName());
                        ingData.put("unit", material.getUnit());

                        BigDecimal totalRequired = (BigDecimal) ingData.getOrDefault("totalRequired", BigDecimal.ZERO);
                        BigDecimal totalUsed = (BigDecimal) ingData.getOrDefault("totalUsed", BigDecimal.ZERO);
                        BigDecimal totalCost = (BigDecimal) ingData.getOrDefault("totalCost", BigDecimal.ZERO);
                        Integer batchCount = (Integer) ingData.getOrDefault("batchCount", 0);

                        ingData.put("totalRequired", totalRequired.add(material.getQuantityRequired()));
                        ingData.put("totalUsed", totalUsed.add(material.getQuantityUsed()));
                        ingData.put("totalCost", totalCost.add(material.getTotalCost()));
                        ingData.put("batchCount", batchCount + 1);
                    }
                });
            }
        });

        // Calculate variance for each material
        materialConsumption.values().forEach(data -> {
            BigDecimal totalRequired = (BigDecimal) data.get("totalRequired");
            BigDecimal totalUsed = (BigDecimal) data.get("totalUsed");
            BigDecimal variance = totalUsed.subtract(totalRequired);
            data.put("variance", variance);

            if (totalRequired.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal variancePercent = variance.divide(totalRequired, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
                data.put("variancePercent", variancePercent);
            } else {
                data.put("variancePercent", BigDecimal.ZERO);
            }
        });

        Map<String, Object> report = new HashMap<>();
        report.put("periodStart", startDate);
        report.put("periodEnd", endDate);
        report.put("businessLocation", businessLocation);
        report.put("totalBatches", batches.size());
        report.put("materials", new ArrayList<>(materialConsumption.values()));

        return ResponseEntity.ok(report);
    }

    @Operation(
        summary = "Cost Variance Report",
        description = """
            Analyzes cost variances between expected and actual production costs.
            Helps identify cost overruns and optimize production planning.
            Includes ingredient cost breakdown and production cost analysis.
            """
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully generated cost variance report")
    })
    @GetMapping("/cost-variance")
    public ResponseEntity<Map<String, Object>> getCostVarianceReport(
            @Parameter(description = "Start date for report", example = "2026-01-01T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date for report", example = "2026-12-31T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Filter by business location")
            @RequestParam(required = false) String businessLocation
    ) {
        List<ProductionBatchDto> batches = productionBatchService.searchProductionBatches(
            businessLocation, null, null, null, null, startDate, endDate
        );

        List<Map<String, Object>> costAnalysis = batches.stream().map(batch -> {
            Map<String, Object> analysis = new HashMap<>();
            analysis.put("batchId", batch.getId());
            analysis.put("referenceNumber", batch.getReferenceNumber());
            analysis.put("productionDate", batch.getProductionDate());
            analysis.put("recipeName", batch.getRecipeName());
            analysis.put("businessLocation", batch.getBusinessLocation());
            analysis.put("actualCost", batch.getTotalCost());
            analysis.put("ingredientCost", batch.getIngredientCost());
            analysis.put("productionCost", batch.getProductionCost());

            return analysis;
        }).collect(Collectors.toList());

        BigDecimal totalActualCost = batches.stream()
            .map(ProductionBatchDto::getTotalCost)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> report = new HashMap<>();
        report.put("periodStart", startDate);
        report.put("periodEnd", endDate);
        report.put("businessLocation", businessLocation);
        report.put("totalBatches", batches.size());
        report.put("totalActualCost", totalActualCost);
        report.put("batches", costAnalysis);

        return ResponseEntity.ok(report);
    }

    /**
     * Production Efficiency Report
     * Analyzes wastage and production efficiency
     */
    @GetMapping("/production-efficiency")
    public ResponseEntity<Map<String, Object>> getProductionEfficiencyReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String businessLocation
    ) {
        List<ProductionBatchDto> batches = productionBatchService.searchProductionBatches(
            businessLocation, null, true, null, null, startDate, endDate
        );

        List<Map<String, Object>> efficiencyData = batches.stream()
            .filter(batch -> batch.getExpectedQuantity() != null && batch.getQuantityProduced() != null)
            .map(batch -> {
                Map<String, Object> data = new HashMap<>();
                data.put("batchId", batch.getId());
                data.put("referenceNumber", batch.getReferenceNumber());
                data.put("productionDate", batch.getProductionDate());
                data.put("recipeName", batch.getRecipeName());
                data.put("expectedQuantity", batch.getExpectedQuantity());
                data.put("producedQuantity", batch.getQuantityProduced());
                data.put("wastedQuantity", batch.getWastedQuantity());
                data.put("unit", batch.getUnit());

                BigDecimal efficiency = batch.getQuantityProduced()
                    .divide(batch.getExpectedQuantity(), 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
                data.put("efficiencyPercent", efficiency);

                if (batch.getWastedQuantity() != null) {
                    BigDecimal wastagePercent = batch.getWastedQuantity()
                        .divide(batch.getExpectedQuantity(), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
                    data.put("wastagePercent", wastagePercent);
                }

                return data;
            })
            .collect(Collectors.toList());

        // Calculate averages
        double avgEfficiency = efficiencyData.stream()
            .map(d -> ((BigDecimal) d.get("efficiencyPercent")).doubleValue())
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);

        Map<String, Object> report = new HashMap<>();
        report.put("periodStart", startDate);
        report.put("periodEnd", endDate);
        report.put("businessLocation", businessLocation);
        report.put("totalBatches", batches.size());
        report.put("averageEfficiency", avgEfficiency);
        report.put("batches", efficiencyData);

        return ResponseEntity.ok(report);
    }

    /**
     * Batch-wise Profitability Analysis
     */
    @GetMapping("/profitability")
    public ResponseEntity<Map<String, Object>> getProfitabilityReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String businessLocation
    ) {
        List<ProductionBatchDto> batches = productionBatchService.searchProductionBatches(
            businessLocation, null, null, null, null, startDate, endDate
        );

        List<Map<String, Object>> profitabilityData = batches.stream().map(batch -> {
            Map<String, Object> data = new HashMap<>();
            data.put("batchId", batch.getId());
            data.put("referenceNumber", batch.getReferenceNumber());
            data.put("productionDate", batch.getProductionDate());
            data.put("recipeName", batch.getRecipeName());
            data.put("productName", batch.getProductName());
            data.put("quantityProduced", batch.getQuantityProduced());
            data.put("unit", batch.getUnit());
            data.put("totalCost", batch.getTotalCost());

            // Unit cost
            if (batch.getQuantityProduced() != null && batch.getQuantityProduced().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal unitCost = batch.getTotalCost()
                    .divide(batch.getQuantityProduced(), 4, RoundingMode.HALF_UP);
                data.put("unitCost", unitCost);
            }

            return data;
        }).collect(Collectors.toList());

        BigDecimal totalCost = batches.stream()
            .map(ProductionBatchDto::getTotalCost)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> report = new HashMap<>();
        report.put("periodStart", startDate);
        report.put("periodEnd", endDate);
        report.put("businessLocation", businessLocation);
        report.put("totalBatches", batches.size());
        report.put("totalProductionCost", totalCost);
        report.put("batches", profitabilityData);

        return ResponseEntity.ok(report);
    }

    /**
     * Recipe Performance Report
     * Shows which recipes are most produced
     */
    @GetMapping("/recipe-performance")
    public ResponseEntity<Map<String, Object>> getRecipePerformanceReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        List<ProductionBatchDto> batches = productionBatchService.searchProductionBatches(
            null, null, null, null, null, startDate, endDate
        );

        // Group by recipe
        Map<Long, Map<String, Object>> recipeStats = new HashMap<>();

        batches.forEach(batch -> {
            Long recipeId = batch.getRecipeId();
            recipeStats.putIfAbsent(recipeId, new HashMap<>());
            Map<String, Object> stats = recipeStats.get(recipeId);

            stats.put("recipeId", recipeId);
            stats.put("recipeName", batch.getRecipeName());
            stats.put("recipeCode", batch.getRecipeCode());

            Integer batchCount = (Integer) stats.getOrDefault("batchCount", 0);
            BigDecimal totalQuantity = (BigDecimal) stats.getOrDefault("totalQuantity", BigDecimal.ZERO);
            BigDecimal totalCost = (BigDecimal) stats.getOrDefault("totalCost", BigDecimal.ZERO);

            stats.put("batchCount", batchCount + 1);
            stats.put("totalQuantity", totalQuantity.add(batch.getQuantityProduced()));
            stats.put("totalCost", totalCost.add(batch.getTotalCost()));
            stats.put("unit", batch.getUnit());
        });

        // Sort by batch count
        List<Map<String, Object>> sortedRecipes = recipeStats.values().stream()
            .sorted((a, b) -> Integer.compare((Integer) b.get("batchCount"), (Integer) a.get("batchCount")))
            .collect(Collectors.toList());

        Map<String, Object> report = new HashMap<>();
        report.put("periodStart", startDate);
        report.put("periodEnd", endDate);
        report.put("totalRecipes", recipeStats.size());
        report.put("recipes", sortedRecipes);

        return ResponseEntity.ok(report);
    }
}

