/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.DispatchInvoiceDto;
import com.pharmacy.dto.FoilPrintingDto;
import com.pharmacy.dto.InventoryStockDto;
import com.pharmacy.dto.IssueMaterialDto;
import com.pharmacy.dto.ProductionBatchDto;
import com.pharmacy.dto.QualityControlDto;
import com.pharmacy.dto.SaleDto;
import com.pharmacy.entity.Sale;
import com.pharmacy.service.InventoryStockService;
import com.pharmacy.service.ProductionBatchService;
import com.pharmacy.service.SaleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Manufacturing Report Controller Provides comprehensive reporting endpoints for manufacturing
 * operations Following pharmaceutical GMP compliance requirements
 */
@RestController
@RequestMapping("/api/v1/manufacturing-reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
    name = "Manufacturing Reports",
    description =
        "Analytics and reporting endpoints for manufacturing operations and GMP compliance (v1)")
public class ManufacturingReportController {

  @Autowired private SaleService saleService;
  @Autowired private ProductionBatchService productionBatchService;
  @Autowired private InventoryStockService inventoryStockService;

  // Add other services as needed (e.g., QCService, DispatchService, etc.)

  @Operation(
      summary = "Material Consumption Report",
      description =
          """
            Comprehensive material consumption analysis across production batches.
            Shows total material usage, variances, and costs with filtering options.
            Useful for inventory management and cost control.
            """)
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully generated material consumption report",
            content = @Content(mediaType = "application/json"))
      })
  @GetMapping("/material-consumption")
  public ResponseEntity<Map<String, Object>> getMaterialConsumptionReport(
      @Parameter(
              description = "Start date for report (ISO 8601 format)",
              example = "2026-01-01T00:00:00")
          @RequestParam(required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime startDate,
      @Parameter(
              description = "End date for report (ISO 8601 format)",
              example = "2026-12-31T23:59:59")
          @RequestParam(required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime endDate,
      @Parameter(description = "Filter by business location", example = "Butt Brothers")
          @RequestParam(required = false)
          String businessLocation,
      @Parameter(description = "Filter by specific ingredient ID") @RequestParam(required = false)
          Long ingredientId) {
    List<ProductionBatchDto> batches =
        productionBatchService.searchProductionBatches(
            businessLocation, null, null, null, null, startDate, endDate);

    // Aggregate material consumption
    Map<Long, Map<String, Object>> materialConsumption = new HashMap<>();

    batches.forEach(
        batch -> {
          if (batch.getMaterialsConsumed() != null) {
            batch
                .getMaterialsConsumed()
                .forEach(
                    material -> {
                      if (ingredientId == null || material.getIngredientId().equals(ingredientId)) {
                        Long ingId = material.getIngredientId();

                        materialConsumption.putIfAbsent(ingId, new HashMap<>());
                        Map<String, Object> ingData = materialConsumption.get(ingId);

                        ingData.put("ingredientId", material.getIngredientId());
                        ingData.put("ingredientName", material.getIngredientName());
                        ingData.put("unit", material.getUnit());

                        BigDecimal totalRequired =
                            (BigDecimal) ingData.getOrDefault("totalRequired", BigDecimal.ZERO);
                        BigDecimal totalUsed =
                            (BigDecimal) ingData.getOrDefault("totalUsed", BigDecimal.ZERO);
                        BigDecimal totalCost =
                            (BigDecimal) ingData.getOrDefault("totalCost", BigDecimal.ZERO);
                        Integer batchCount = (Integer) ingData.getOrDefault("batchCount", 0);

                        ingData.put(
                            "totalRequired", totalRequired.add(material.getQuantityRequired()));
                        ingData.put("totalUsed", totalUsed.add(material.getQuantityUsed()));
                        ingData.put("totalCost", totalCost.add(material.getTotalCost()));
                        ingData.put("batchCount", batchCount + 1);
                      }
                    });
          }
        });

    // Calculate variance for each material
    materialConsumption
        .values()
        .forEach(
            data -> {
              BigDecimal totalRequired = (BigDecimal) data.get("totalRequired");
              BigDecimal totalUsed = (BigDecimal) data.get("totalUsed");
              BigDecimal variance = totalUsed.subtract(totalRequired);
              data.put("variance", variance);

              if (totalRequired.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal variancePercent =
                    variance
                        .divide(totalRequired, 4, RoundingMode.HALF_UP)
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
      description =
          """
            Analyzes cost variances between expected and actual production costs.
            Helps identify cost overruns and optimize production planning.
            Includes ingredient cost breakdown and production cost analysis.
            """)
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully generated cost variance report",
            content = @Content(mediaType = "application/json"))
      })
  @GetMapping("/cost-variance")
  public ResponseEntity<Map<String, Object>> getCostVarianceReport(
      @Parameter(description = "Start date for report", example = "2026-01-01T00:00:00")
          @RequestParam(required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime startDate,
      @Parameter(description = "End date for report", example = "2026-12-31T23:59:59")
          @RequestParam(required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime endDate,
      @Parameter(description = "Filter by business location") @RequestParam(required = false)
          String businessLocation) {
    List<ProductionBatchDto> batches =
        productionBatchService.searchProductionBatches(
            businessLocation, null, null, null, null, startDate, endDate);

    List<Map<String, Object>> costAnalysis =
        batches.stream()
            .map(
                batch -> {
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
                })
            .collect(Collectors.toList());

    BigDecimal totalActualCost =
        batches.stream()
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

  /** Production Efficiency Report Analyzes wastage and production efficiency */
  @GetMapping("/production-efficiency")
  public ResponseEntity<Map<String, Object>> getProductionEfficiencyReport(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime endDate,
      @RequestParam(required = false) String businessLocation) {
    List<ProductionBatchDto> batches =
        productionBatchService.searchProductionBatches(
            businessLocation, null, true, null, null, startDate, endDate);

    List<Map<String, Object>> efficiencyData =
        batches.stream()
            .filter(
                batch -> batch.getExpectedQuantity() != null && batch.getQuantityProduced() != null)
            .map(
                batch -> {
                  Map<String, Object> data = new HashMap<>();
                  data.put("batchId", batch.getId());
                  data.put("referenceNumber", batch.getReferenceNumber());
                  data.put("productionDate", batch.getProductionDate());
                  data.put("recipeName", batch.getRecipeName());
                  data.put("expectedQuantity", batch.getExpectedQuantity());
                  data.put("producedQuantity", batch.getQuantityProduced());
                  data.put("wastedQuantity", batch.getWastedQuantity());
                  data.put("unit", batch.getUnit());

                  BigDecimal efficiency =
                      batch
                          .getQuantityProduced()
                          .divide(batch.getExpectedQuantity(), 4, RoundingMode.HALF_UP)
                          .multiply(new BigDecimal("100"));
                  data.put("efficiencyPercent", efficiency);

                  if (batch.getWastedQuantity() != null) {
                    BigDecimal wastagePercent =
                        batch
                            .getWastedQuantity()
                            .divide(batch.getExpectedQuantity(), 4, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal("100"));
                    data.put("wastagePercent", wastagePercent);
                  }

                  return data;
                })
            .collect(Collectors.toList());

    // Calculate averages
    double avgEfficiency =
        efficiencyData.stream()
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

  /** Batch-wise Profitability Analysis */
  @GetMapping("/profitability")
  public ResponseEntity<Map<String, Object>> getProfitabilityReport(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime endDate,
      @RequestParam(required = false) String businessLocation) {
    List<ProductionBatchDto> batches =
        productionBatchService.searchProductionBatches(
            businessLocation, null, null, null, null, startDate, endDate);

    List<Map<String, Object>> profitabilityData =
        batches.stream()
            .map(
                batch -> {
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
                  if (batch.getQuantityProduced() != null
                      && batch.getQuantityProduced().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal unitCost =
                        batch
                            .getTotalCost()
                            .divide(batch.getQuantityProduced(), 4, RoundingMode.HALF_UP);
                    data.put("unitCost", unitCost);
                  }

                  return data;
                })
            .collect(Collectors.toList());

    BigDecimal totalCost =
        batches.stream()
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

  /** Recipe Performance Report Shows which recipes are most produced */
  @GetMapping("/recipe-performance")
  public ResponseEntity<Map<String, Object>> getRecipePerformanceReport(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime endDate) {
    List<ProductionBatchDto> batches =
        productionBatchService.searchProductionBatches(
            null, null, null, null, null, startDate, endDate);

    // Group by recipe
    Map<Long, Map<String, Object>> recipeStats = new HashMap<>();

    batches.forEach(
        batch -> {
          Long recipeId = batch.getRecipeId();
          recipeStats.putIfAbsent(recipeId, new HashMap<>());
          Map<String, Object> stats = recipeStats.get(recipeId);

          stats.put("recipeId", recipeId);
          stats.put("recipeName", batch.getRecipeName());
          stats.put("recipeCode", batch.getRecipeCode());

          Integer batchCount = (Integer) stats.getOrDefault("batchCount", 0);
          BigDecimal totalQuantity =
              (BigDecimal) stats.getOrDefault("totalQuantity", BigDecimal.ZERO);
          BigDecimal totalCost = (BigDecimal) stats.getOrDefault("totalCost", BigDecimal.ZERO);

          stats.put("batchCount", batchCount + 1);
          stats.put("totalQuantity", totalQuantity.add(batch.getQuantityProduced()));
          stats.put("totalCost", totalCost.add(batch.getTotalCost()));
          stats.put("unit", batch.getUnit());
        });

    // Sort by batch count
    List<Map<String, Object>> sortedRecipes =
        recipeStats.values().stream()
            .sorted(
                (a, b) ->
                    Integer.compare((Integer) b.get("batchCount"), (Integer) a.get("batchCount")))
            .collect(Collectors.toList());

    Map<String, Object> report = new HashMap<>();
    report.put("periodStart", startDate);
    report.put("periodEnd", endDate);
    report.put("totalRecipes", recipeStats.size());
    report.put("recipes", sortedRecipes);

    return ResponseEntity.ok(report);
  }

  @Operation(
      summary = "Demo Manufacturing Workflow",
      description =
          "Runs a complete demo of the manufacturing workflow, simulating all key steps for training or demonstration purposes. Steps include: Create Sales Order, Generate Work Order, Issue Materials, Foil Printing/Production, Quality Control, Receive Finished Goods, Dispatch and Invoice.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Demo workflow executed successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(
                            type = "object",
                            example =
                                "{\"status\":\"success\",\"steps\":[\"Create Sales Order\",\"Generate Work Order\",\"Issue Materials\",\"Foil Printing/Production\",\"Quality Control\",\"Receive Finished Goods\",\"Dispatch and Invoice\"]}")))
      })
  @PostMapping("/demo-workflow")
  public ResponseEntity<Map<String, Object>> runDemoWorkflow() {
    List<String> steps =
        Arrays.asList(
            "Create Sales Order",
            "Generate Work Order",
            "Issue Materials",
            "Foil Printing/Production",
            "Quality Control",
            "Receive Finished Goods",
            "Dispatch and Invoice");
    Map<String, Object> result = new HashMap<>();
    result.put("status", "success");
    result.put("steps", steps);
    result.put(
        "message", "Demo workflow executed. All steps simulated for training/demo purposes.");
    return ResponseEntity.ok(result);
  }

  @Operation(
      summary = "Create Sales Order (Manufacturing Step 1)",
      description = "Step 1: Create a sales order as the first step in the manufacturing workflow.",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Sales order request body",
              required = true,
              content =
                  @Content(
                      schema = @Schema(implementation = SaleDto.class),
                      examples =
                          @io.swagger.v3.oas.annotations.media.ExampleObject(
                              value =
                                  "{\"storeId\":1,\"customerId\":2,\"pharmacistId\":3,\"subtotal\":100.00,\"discount\":5.00,\"taxAmount\":10.00,\"totalAmount\":105.00,\"paymentMethod\":\"CASH\",\"notes\":\"Urgent\",\"items\":[{\"productId\":10,\"quantity\":2,\"unitPrice\":50.00,\"totalPrice\":100.00,\"productName\":\"Paracetamol\",\"productCode\":\"P001\"}]}"))))
  @ApiResponse(
      responseCode = "200",
      description = "Sales order created",
      content =
          @Content(
              schema =
                  @Schema(
                      type = "object",
                      example = "{\"status\":\"success\",\"salesOrderId\":123}")))
  @PostMapping("/step/create-sales-order")
  public ResponseEntity<Map<String, Object>> createSalesOrder(
      @RequestBody SaleDto salesOrderRequest) {
    SaleDto sale = saleService.createSale(salesOrderRequest);
    Map<String, Object> result = new HashMap<>();
    result.put("status", "success");
    result.put("salesOrderId", sale.getId());
    return ResponseEntity.ok(result);
  }

  @Operation(
      summary = "Generate Work Order (Manufacturing Step 2)",
      description = "Step 2: Generate a work order/job card for the manufacturing batch.")
  @ApiResponse(
      responseCode = "200",
      description = "Work order generated",
      content =
          @Content(
              schema =
                  @Schema(
                      type = "object",
                      example = "{\"status\":\"success\",\"workOrderId\":456}")))
  @PostMapping("/step/generate-work-order")
  public ResponseEntity<Map<String, Object>> generateWorkOrder(
      @RequestBody ProductionBatchDto workOrderRequest) {
    ProductionBatchDto batch = productionBatchService.create(workOrderRequest);
    Map<String, Object> result = new HashMap<>();
    result.put("status", "success");
    result.put("workOrderId", batch.getId());
    return ResponseEntity.ok(result);
  }

  @Operation(
      summary = "Issue Materials (Manufacturing Step 3)",
      description = "Step 3: Issue materials to production for the batch.",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Issue material request body",
              required = true,
              content =
                  @Content(
                      schema = @Schema(implementation = IssueMaterialDto.class),
                      examples =
                          @io.swagger.v3.oas.annotations.media.ExampleObject(
                              value =
                                  "{\"stockId\":1,\"quantity\":10,\"reason\":\"Issued to production\"}"))))
  @ApiResponse(
      responseCode = "200",
      description = "Materials issued",
      content =
          @Content(
              schema =
                  @Schema(type = "object", example = "{\"status\":\"success\",\"issued\":true}")))
  @PostMapping("/step/issue-materials")
  public ResponseEntity<Map<String, Object>> issueMaterials(
      @RequestBody IssueMaterialDto issueRequest) {
    if (issueRequest.getStockId() != null && issueRequest.getQuantity() != null) {
      inventoryStockService.adjustStock(
          issueRequest.getStockId(),
          -issueRequest.getQuantity(),
          issueRequest.getReason() != null ? issueRequest.getReason() : "Issued to production");
    }
    Map<String, Object> result = new HashMap<>();
    result.put("status", "success");
    result.put("issued", true);
    return ResponseEntity.ok(result);
  }

  @Operation(
      summary = "Foil Printing/Production (Manufacturing Step 4)",
      description = "Step 4: Perform foil printing/production as a single step for this process.",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Foil printing request body",
              required = true,
              content =
                  @Content(
                      schema = @Schema(implementation = FoilPrintingDto.class),
                      examples =
                          @io.swagger.v3.oas.annotations.media.ExampleObject(
                              value =
                                  "{\"id\":1,\"jobNumber\":\"FP-001\",\"materialCode\":\"M001\",\"foilType\":\"Silver\",\"sheetsUsed\":100,\"bundlesProduced\":10,\"cost\":500.00,\"createdAt\":\"2026-01-26T10:00:00\",\"updatedAt\":\"2026-01-26T12:00:00\"}"))))
  @ApiResponse(
      responseCode = "200",
      description = "Foil printing/production completed",
      content =
          @Content(
              schema =
                  @Schema(
                      type = "object",
                      example = "{\"status\":\"success\",\"foilPrinted\":true}")))
  @PostMapping("/step/foil-printing")
  public ResponseEntity<Map<String, Object>> foilPrinting(
      @RequestBody FoilPrintingDto foilRequest) {
    if (foilRequest.getId() != null) {
      ProductionBatchDto batch = productionBatchService.getById(foilRequest.getId());
      batch.setStatus("FOIL_PRINTED");
      productionBatchService.update(foilRequest.getId(), batch);
    }
    Map<String, Object> result = new HashMap<>();
    result.put("status", "success");
    result.put("foilPrinted", true);
    return ResponseEntity.ok(result);
  }

  @Operation(
      summary = "Quality Control (Manufacturing Step 5)",
      description = "Step 5: Perform quality control for the batch.",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Quality control request body",
              required = true,
              content =
                  @Content(
                      schema = @Schema(implementation = QualityControlDto.class),
                      examples =
                          @io.swagger.v3.oas.annotations.media.ExampleObject(
                              value = "{\"batchId\":1,\"qcPassed\":true}"))))
  @ApiResponse(
      responseCode = "200",
      description = "Quality control completed",
      content =
          @Content(
              schema =
                  @Schema(type = "object", example = "{\"status\":\"success\",\"qcPassed\":true}")))
  @PostMapping("/step/quality-control")
  public ResponseEntity<Map<String, Object>> qualityControl(
      @RequestBody QualityControlDto qcRequest) {
    if (qcRequest.getBatchId() != null && qcRequest.getQcPassed() != null) {
      ProductionBatchDto batch = productionBatchService.getById(qcRequest.getBatchId());
      batch.setStatus(qcRequest.getQcPassed() ? "QC_PASSED" : "QC_FAILED");
      productionBatchService.update(qcRequest.getBatchId(), batch);
    }
    Map<String, Object> result = new HashMap<>();
    result.put("status", "success");
    result.put("qcPassed", qcRequest.getQcPassed() != null ? qcRequest.getQcPassed() : false);
    return ResponseEntity.ok(result);
  }

  @Operation(
      summary = "Receive Finished Goods (Manufacturing Step 6)",
      description = "Step 6: Receive finished goods into inventory.")
  @ApiResponse(
      responseCode = "200",
      description = "Finished goods received",
      content =
          @Content(
              schema =
                  @Schema(type = "object", example = "{\"status\":\"success\",\"received\":true}")))
  @PostMapping("/step/receive-finished-goods")
  public ResponseEntity<Map<String, Object>> receiveFinishedGoods(
      @RequestBody InventoryStockDto receiveRequest) {
    InventoryStockDto stock = inventoryStockService.addInventoryStock(receiveRequest);
    Map<String, Object> result = new HashMap<>();
    result.put("status", "success");
    result.put("received", true);
    result.put("stockId", stock.getId());
    return ResponseEntity.ok(result);
  }

  @Operation(
      summary = "Dispatch and Invoice (Manufacturing Step 7)",
      description = "Step 7: Dispatch finished goods and generate invoice.",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Dispatch and invoice request body",
              required = true,
              content =
                  @Content(
                      schema = @Schema(implementation = DispatchInvoiceDto.class),
                      examples =
                          @io.swagger.v3.oas.annotations.media.ExampleObject(
                              value = "{\"saleId\":123,\"status\":\"DISPATCHED\"}"))))
  @ApiResponse(
      responseCode = "200",
      description = "Goods dispatched and invoice generated",
      content =
          @Content(
              schema =
                  @Schema(
                      type = "object",
                      example = "{\"status\":\"success\",\"dispatched\":true,\"invoiceId\":789}")))
  @PostMapping("/step/dispatch-invoice")
  public ResponseEntity<Map<String, Object>> dispatchAndInvoice(
      @RequestBody DispatchInvoiceDto dispatchRequest) {
    if (dispatchRequest.getSaleId() != null && dispatchRequest.getStatus() != null) {
      saleService.updateSaleStatus(
          dispatchRequest.getSaleId(), Sale.SaleStatus.valueOf(dispatchRequest.getStatus()));
    }
    Map<String, Object> result = new HashMap<>();
    result.put("status", "success");
    result.put("dispatched", true);
    result.put("invoiceId", dispatchRequest.getSaleId());
    return ResponseEntity.ok(result);
  }
}
