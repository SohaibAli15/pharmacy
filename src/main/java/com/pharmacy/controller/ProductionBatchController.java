package com.pharmacy.controller;

import com.pharmacy.dto.ProductionBatchDto;
import com.pharmacy.service.ProductionBatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/production-batches")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Production Batches", description = "APIs for managing production batches and material consumption tracking (v1)")
public class ProductionBatchController {

    private final ProductionBatchService productionBatchService;

    @Operation(
        summary = "Get all production batches",
        description = "Retrieve all production batches with complete details"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved production batches",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductionBatchDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<ProductionBatchDto>> listAll() {
        List<ProductionBatchDto> batches = productionBatchService.listAll();
        return ResponseEntity.ok(batches);
    }

    @Operation(
        summary = "Search production batches",
        description = "Search production batches with multiple filter criteria including location, date range, and status"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered production batches")
    })
    @GetMapping("/search")
    public ResponseEntity<List<ProductionBatchDto>> search(
            @Parameter(description = "Filter by business location", example = "Butt Brothers")
            @RequestParam(required = false) String businessLocation,
            @Parameter(description = "Filter by status (DRAFT, IN_PROGRESS, COMPLETED, FINALIZED)")
            @RequestParam(required = false) String status,
            @Parameter(description = "Filter by finalization status")
            @RequestParam(required = false) Boolean finalized,
            @Parameter(description = "Filter by recipe ID")
            @RequestParam(required = false) Long recipeId,
            @Parameter(description = "Filter by product ID")
            @RequestParam(required = false) Long productId,
            @Parameter(description = "Start date for date range filter (ISO 8601 format)", example = "2026-01-01T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date for date range filter (ISO 8601 format)", example = "2026-12-31T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        List<ProductionBatchDto> batches = productionBatchService.searchProductionBatches(
            businessLocation, status, finalized, recipeId, productId, startDate, endDate
        );
        return ResponseEntity.ok(batches);
    }

    @Operation(
        summary = "Get production batches by recipe",
        description = "Retrieve all production batches for a specific recipe"
    )
    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<List<ProductionBatchDto>> getByRecipe(
            @Parameter(description = "Recipe ID") @PathVariable Long recipeId) {
        List<ProductionBatchDto> batches = productionBatchService.getByRecipe(recipeId);
        return ResponseEntity.ok(batches);
    }

    @Operation(
        summary = "Get production batches by status",
        description = "Retrieve all production batches with a specific status"
    )
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProductionBatchDto>> getByStatus(
            @Parameter(description = "Status value", example = "COMPLETED") @PathVariable String status) {
        List<ProductionBatchDto> batches = productionBatchService.getByStatus(status);
        return ResponseEntity.ok(batches);
    }

    @Operation(
        summary = "Get all business locations",
        description = "Retrieve list of all business locations that have production batches"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved locations")
    })
    @GetMapping("/locations")
    public ResponseEntity<List<String>> getBusinessLocations() {
        List<String> locations = productionBatchService.getAllBusinessLocations();
        return ResponseEntity.ok(locations);
    }

    @Operation(
        summary = "Get production batch by ID",
        description = "Retrieve a specific production batch with all material consumption details"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Production batch found"),
        @ApiResponse(responseCode = "404", description = "Production batch not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductionBatchDto> get(
            @Parameter(description = "Production batch ID", example = "1") @PathVariable Long id) {
        try {
            ProductionBatchDto dto = productionBatchService.getById(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Create new production batch",
        description = "Create a new production batch with material consumption tracking"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Production batch created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<?> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Production batch details with materials consumed",
                required = true
            )
            @Valid @RequestBody ProductionBatchDto dto) {
        try {
            ProductionBatchDto created = productionBatchService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @Operation(
        summary = "Update production batch",
        description = "Update production batch details and material consumption (only if not finalized)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Production batch updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or batch is finalized"),
        @ApiResponse(responseCode = "404", description = "Production batch not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Parameter(description = "Production batch ID") @PathVariable Long id,
            @Valid @RequestBody ProductionBatchDto dto) {
        try {
            ProductionBatchDto updated = productionBatchService.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @Operation(
        summary = "Delete production batch",
        description = "Delete a production batch (only if not finalized)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Production batch deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Cannot delete finalized batch"),
        @ApiResponse(responseCode = "404", description = "Production batch not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "Production batch ID") @PathVariable Long id) {
        try {
            productionBatchService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @Operation(
        summary = "Finalize production batch",
        description = """
            Finalize production batch - locks the batch from further edits and updates inventory.
            This action:
            - Deducts consumed materials from inventory
            - Adds produced quantity to finished goods inventory
            - Locks the batch from editing
            - Records finalization timestamp and user
            """
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Production batch finalized successfully"),
        @ApiResponse(responseCode = "400", description = "Batch already finalized or invalid state"),
        @ApiResponse(responseCode = "404", description = "Production batch not found")
    })
    @PostMapping("/{id}/finalize")
    public ResponseEntity<?> finalize(
            @Parameter(description = "Production batch ID") @PathVariable Long id,
            @Parameter(description = "Username of person finalizing", example = "admin")
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

    @Operation(
        summary = "Unfinalize production batch",
        description = """
            Reverse the finalization of a production batch.
            This action:
            - Restores consumed materials to inventory
            - Removes produced quantity from finished goods
            - Unlocks the batch for editing
            - Clears finalization details
            """
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Production batch unfinalized successfully"),
        @ApiResponse(responseCode = "400", description = "Batch is not finalized or cannot be unfinalized"),
        @ApiResponse(responseCode = "404", description = "Production batch not found")
    })
    @PostMapping("/{id}/unfinalize")
    public ResponseEntity<?> unfinalize(
            @Parameter(description = "Production batch ID") @PathVariable Long id) {
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
