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

import com.pharmacy.dto.InventoryStockDto;
import com.pharmacy.dto.ProductionBatchDto;
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
      description = "Step 1: Create a sales order as the first step in the manufacturing workflow.")
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
      @RequestBody Map<String, Object> salesOrderRequest) {
    // Convert request map to SaleDto (manual mapping for now)
    SaleDto saleDto = new SaleDto();
    saleDto.setStoreId(
        salesOrderRequest.get("storeId") != null
            ? Long.valueOf(salesOrderRequest.get("storeId").toString())
            : null);
    saleDto.setCustomerId(
        salesOrderRequest.get("customerId") != null
            ? Long.valueOf(salesOrderRequest.get("customerId").toString())
            : null);
    saleDto.setPharmacistId(
        salesOrderRequest.get("pharmacistId") != null
            ? Long.valueOf(salesOrderRequest.get("pharmacistId").toString())
            : null);
    saleDto.setSubtotal(
        salesOrderRequest.get("subtotal") != null
            ? new BigDecimal(salesOrderRequest.get("subtotal").toString())
            : null);
    saleDto.setDiscount(
        salesOrderRequest.get("discount") != null
            ? new BigDecimal(salesOrderRequest.get("discount").toString())
            : null);
    saleDto.setTaxAmount(
        salesOrderRequest.get("taxAmount") != null
            ? new BigDecimal(salesOrderRequest.get("taxAmount").toString())
            : null);
    saleDto.setTotalAmount(
        salesOrderRequest.get("totalAmount") != null
            ? new BigDecimal(salesOrderRequest.get("totalAmount").toString())
            : null);
    saleDto.setPaymentMethod(
        salesOrderRequest.get("paymentMethod") != null
            ? Sale.PaymentMethod.valueOf(salesOrderRequest.get("paymentMethod").toString())
            : null);
    saleDto.setNotes((String) salesOrderRequest.get("notes"));
    // TODO: Map items if present
    SaleDto sale = saleService.createSale(saleDto);
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
      description = "Step 3: Issue materials to production for the batch.")
  @ApiResponse(
      responseCode = "200",
      description = "Materials issued",
      content =
          @Content(
              schema =
                  @Schema(type = "object", example = "{\"status\":\"success\",\"issued\":true}")))
  @PostMapping("/step/issue-materials")
  public ResponseEntity<Map<String, Object>> issueMaterials(
      @RequestBody Map<String, Object> issueRequest) {
    // Example expects: {stockId, quantity, reason}
    Long stockId =
        issueRequest.get("stockId") != null
            ? Long.valueOf(issueRequest.get("stockId").toString())
            : null;
    Integer quantity =
        issueRequest.get("quantity") != null
            ? Integer.valueOf(issueRequest.get("quantity").toString())
            : null;
    String reason = (String) issueRequest.get("reason");
    if (stockId != null && quantity != null) {
      inventoryStockService.adjustStock(
          stockId, -quantity, reason != null ? reason : "Issued to production");
    }
    Map<String, Object> result = new HashMap<>();
    result.put("status", "success");
    result.put("issued", true);
    return ResponseEntity.ok(result);
  }

  @Operation(
      summary = "Foil Printing/Production (Manufacturing Step 4)",
      description = "Step 4: Perform foil printing/production as a single step for this process.")
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
      @RequestBody Map<String, Object> foilRequest) {
    // Example expects: {batchId, status}
    Long batchId =
        foilRequest.get("batchId") != null
            ? Long.valueOf(foilRequest.get("batchId").toString())
            : null;
    String status = (String) foilRequest.get("status");
    if (batchId != null && status != null) {
      ProductionBatchDto batch = productionBatchService.getById(batchId);
      batch.setStatus(status);
      productionBatchService.update(batchId, batch);
    }
    Map<String, Object> result = new HashMap<>();
    result.put("status", "success");
    result.put("foilPrinted", true);
    return ResponseEntity.ok(result);
  }

  @Operation(
      summary = "Quality Control (Manufacturing Step 5)",
      description = "Step 5: Perform quality control for the batch.")
  @ApiResponse(
      responseCode = "200",
      description = "Quality control completed",
      content =
          @Content(
              schema =
                  @Schema(type = "object", example = "{\"status\":\"success\",\"qcPassed\":true}")))
  @PostMapping("/step/quality-control")
  public ResponseEntity<Map<String, Object>> qualityControl(
      @RequestBody Map<String, Object> qcRequest) {
    // Example expects: {batchId, qcPassed}
    Long batchId =
        qcRequest.get("batchId") != null ? Long.valueOf(qcRequest.get("batchId").toString()) : null;
    Boolean qcPassed =
        qcRequest.get("qcPassed") != null
            ? Boolean.valueOf(qcRequest.get("qcPassed").toString())
            : null;
    if (batchId != null && qcPassed != null) {
      ProductionBatchDto batch = productionBatchService.getById(batchId);
      batch.setStatus(qcPassed ? "QC_PASSED" : "QC_FAILED");
      productionBatchService.update(batchId, batch);
    }
    Map<String, Object> result = new HashMap<>();
    result.put("status", "success");
    result.put("qcPassed", qcPassed != null ? qcPassed : false);
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
      description = "Step 7: Dispatch finished goods and generate invoice.")
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
      @RequestBody Map<String, Object> dispatchRequest) {
    // Example expects: {saleId, status}
    Long saleId =
        dispatchRequest.get("saleId") != null
            ? Long.valueOf(dispatchRequest.get("saleId").toString())
            : null;
    String status = (String) dispatchRequest.get("status");
    if (saleId != null && status != null) {
      saleService.updateSaleStatus(saleId, Sale.SaleStatus.valueOf(status));
    }
    Map<String, Object> result = new HashMap<>();
    result.put("status", "success");
    result.put("dispatched", true);
    result.put("invoiceId", saleId);
    return ResponseEntity.ok(result);
  }
}
