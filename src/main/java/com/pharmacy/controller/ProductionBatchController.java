package com.pharmacy.controller;

import com.pharmacy.dto.ProductionBatchDto;
import com.pharmacy.service.ProductionBatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/production-batches")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductionBatchController {

    private final ProductionBatchService productionBatchService;

    /**
     * Get all production batches
     */
    @GetMapping
    public ResponseEntity<List<ProductionBatchDto>> listAll() {
        List<ProductionBatchDto> batches = productionBatchService.listAll();
        return ResponseEntity.ok(batches);
    }

    /**
     * Search production batches with filters
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductionBatchDto>> search(
            @RequestParam(required = false) String businessLocation,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Boolean finalized,
            @RequestParam(required = false) Long recipeId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        List<ProductionBatchDto> batches = productionBatchService.searchProductionBatches(
            businessLocation, status, finalized, recipeId, productId, startDate, endDate
        );
        return ResponseEntity.ok(batches);
    }

    /**
     * Get production batches by recipe
     */
    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<List<ProductionBatchDto>> getByRecipe(@PathVariable Long recipeId) {
        List<ProductionBatchDto> batches = productionBatchService.getByRecipe(recipeId);
        return ResponseEntity.ok(batches);
    }

    /**
     * Get production batches by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProductionBatchDto>> getByStatus(@PathVariable String status) {
        List<ProductionBatchDto> batches = productionBatchService.getByStatus(status);
        return ResponseEntity.ok(batches);
    }

    /**
     * Get all business locations
     */
    @GetMapping("/locations")
    public ResponseEntity<List<String>> getBusinessLocations() {
        List<String> locations = productionBatchService.getAllBusinessLocations();
        return ResponseEntity.ok(locations);
    }

    /**
     * Get production batch by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductionBatchDto> get(@PathVariable Long id) {
        try {
            ProductionBatchDto dto = productionBatchService.getById(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create new production batch
     */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ProductionBatchDto dto) {
        try {
            ProductionBatchDto created = productionBatchService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Update production batch
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ProductionBatchDto dto) {
        try {
            ProductionBatchDto updated = productionBatchService.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Delete production batch
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            productionBatchService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Finalize production batch
     */
    @PostMapping("/{id}/finalize")
    public ResponseEntity<?> finalize(
            @PathVariable Long id,
            @RequestParam(required = false) String finalizedBy
    ) {
        try {
            ProductionBatchDto finalized = productionBatchService.finalize(id, finalizedBy);
            return ResponseEntity.ok(finalized);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Unfinalize production batch
     */
    @PostMapping("/{id}/unfinalize")
    public ResponseEntity<?> unfinalize(@PathVariable Long id) {
        try {
            ProductionBatchDto unfinalized = productionBatchService.unfinalize(id);
            return ResponseEntity.ok(unfinalized);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Get production statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        List<ProductionBatchDto> batches;

        if (startDate != null && endDate != null) {
            batches = productionBatchService.searchProductionBatches(
                null, null, null, null, null, startDate, endDate
            );
        } else {
            batches = productionBatchService.listAll();
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBatches", batches.size());
        stats.put("finalizedBatches", batches.stream().filter(ProductionBatchDto::getIsFinalized).count());
        stats.put("pendingBatches", batches.stream().filter(b -> !b.getIsFinalized()).count());

        // Group by status
        Map<String, Long> statusCount = new HashMap<>();
        batches.forEach(batch -> {
            String status = batch.getStatus() != null ? batch.getStatus() : "UNKNOWN";
            statusCount.put(status, statusCount.getOrDefault(status, 0L) + 1);
        });
        stats.put("statusBreakdown", statusCount);

        // Group by location
        Map<String, Long> locationCount = new HashMap<>();
        batches.forEach(batch -> {
            String location = batch.getBusinessLocation() != null ? batch.getBusinessLocation() : "Unknown";
            locationCount.put(location, locationCount.getOrDefault(location, 0L) + 1);
        });
        stats.put("locationBreakdown", locationCount);

        return ResponseEntity.ok(stats);
    }
}
