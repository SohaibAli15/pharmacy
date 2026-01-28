/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.ProductionBatchDto;
import com.pharmacy.dto.ProductionBatchMaterialDto;
import com.pharmacy.entity.*;
import com.pharmacy.exception.NotFoundException;
import com.pharmacy.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductionBatchService {

  private final ProductionBatchRepository productionBatchRepository;
  private final RecipeRepository recipeRepository;
  private final ProductRepository productRepository;
  private final IngredientRepository ingredientRepository;
  private final ProductionBatchMaterialRepository materialRepository;

  public ProductionBatchDto toDto(ProductionBatch pb) {
    ProductionBatchDto dto = new ProductionBatchDto();
    dto.setId(pb.getId());
    dto.setRecipeId(pb.getRecipe().getId());
    dto.setRecipeName(pb.getRecipe().getName());
    dto.setRecipeCode(pb.getRecipe().getRecipeCode());
    dto.setReferenceNumber(pb.getReferenceNumber());
    dto.setProductionDate(pb.getProductionDate());
    dto.setBusinessLocation(pb.getBusinessLocation());

    if (pb.getProduct() != null) {
      dto.setProductId(pb.getProduct().getId());
      dto.setProductName(pb.getProduct().getName());
    }

    dto.setQuantityProduced(pb.getQuantityProduced());
    dto.setExpectedQuantity(pb.getExpectedQuantity());
    dto.setWastedQuantity(pb.getWastedQuantity());
    dto.setUnit(pb.getUnit());
    dto.setTotalCost(pb.getTotalCost());
    dto.setProductionCost(pb.getProductionCost());
    dto.setIngredientCost(pb.getIngredientCost());
    dto.setStatus(pb.getStatus());
    dto.setIsFinalized(pb.getIsFinalized());
    dto.setFinalizedAt(pb.getFinalizedAt());
    dto.setFinalizedBy(pb.getFinalizedBy());
    dto.setLotNumber(pb.getLotNumber());
    dto.setAttachedDocumentPath(pb.getAttachedDocumentPath());
    dto.setNotes(pb.getNotes());

    if (pb.getMaterialsConsumed() != null) {
      dto.setMaterialsConsumed(
          pb.getMaterialsConsumed().stream().map(this::toMaterialDto).collect(Collectors.toList()));
    }

    dto.setCreatedAt(pb.getCreatedAt());
    dto.setUpdatedAt(pb.getUpdatedAt());
    dto.setCreatedBy(pb.getCreatedBy());
    dto.setUpdatedBy(pb.getUpdatedBy());
    if (pb.getWorkOrder() != null) {
      dto.setWorkOrderId(pb.getWorkOrder().getId());
    }
    return dto;
  }

  public ProductionBatchMaterialDto toMaterialDto(ProductionBatchMaterial pbm) {
    ProductionBatchMaterialDto dto = new ProductionBatchMaterialDto();
    dto.setId(pbm.getId());
    dto.setIngredientId(pbm.getIngredient().getId());
    dto.setIngredientName(pbm.getIngredient().getName());
    dto.setQuantityRequired(pbm.getQuantityRequired());
    dto.setQuantityUsed(pbm.getQuantityUsed());
    dto.setUnit(pbm.getUnit());
    dto.setVariance(pbm.getVariance());
    dto.setVariancePercent(pbm.getVariancePercent());
    dto.setCostPerUnit(pbm.getCostPerUnit());
    dto.setTotalCost(pbm.getTotalCost());
    dto.setLotNumber(pbm.getLotNumber());
    dto.setNotes(pbm.getNotes());
    return dto;
  }

  public ProductionBatch fromDto(ProductionBatchDto dto) {
    ProductionBatch pb = new ProductionBatch();
    pb.setId(dto.getId());

    Recipe recipe =
        recipeRepository
            .findById(dto.getRecipeId())
            .orElseThrow(() -> new RuntimeException("Recipe not found: " + dto.getRecipeId()));
    pb.setRecipe(recipe);

    if (dto.getProductId() != null) {
      Product product = productRepository.findById(dto.getProductId()).orElse(null);
      pb.setProduct(product);
    } else if (recipe.getProduct() != null) {
      pb.setProduct(recipe.getProduct());
    }

    // Generate reference number if not provided
    if (dto.getReferenceNumber() == null || dto.getReferenceNumber().isEmpty()) {
      pb.setReferenceNumber(generateReferenceNumber());
    } else {
      pb.setReferenceNumber(dto.getReferenceNumber());
    }

    pb.setProductionDate(dto.getProductionDate());
    pb.setBusinessLocation(dto.getBusinessLocation());
    pb.setQuantityProduced(dto.getQuantityProduced());
    pb.setExpectedQuantity(
        dto.getExpectedQuantity() != null ? dto.getExpectedQuantity() : recipe.getOutputQuantity());
    pb.setWastedQuantity(dto.getWastedQuantity());
    pb.setUnit(dto.getUnit() != null ? dto.getUnit() : recipe.getOutputUnit());
    pb.setStatus(dto.getStatus() != null ? dto.getStatus() : "DRAFT");
    pb.setIsFinalized(dto.getIsFinalized() != null ? dto.getIsFinalized() : false);
    pb.setLotNumber(dto.getLotNumber());
    pb.setAttachedDocumentPath(dto.getAttachedDocumentPath());
    pb.setNotes(dto.getNotes());
    pb.setCreatedBy(dto.getCreatedBy());
    pb.setUpdatedBy(dto.getUpdatedBy());
    // Attach work order if provided
    if (dto.getWorkOrderId() != null) {
      WorkOrder workOrder = new WorkOrder();
      workOrder.setId(dto.getWorkOrderId());
      pb.setWorkOrder(workOrder);
    }
    return pb;
  }

  public ProductionBatchDto create(ProductionBatchDto dto) {
    // Validate reference number uniqueness
    if (dto.getReferenceNumber() != null && !dto.getReferenceNumber().isEmpty()) {
      if (productionBatchRepository.findByReferenceNumber(dto.getReferenceNumber()).isPresent()) {
        throw new RuntimeException("Reference number already exists: " + dto.getReferenceNumber());
      }
    }

    ProductionBatch pb = fromDto(dto);
    Recipe recipe = pb.getRecipe();

    // Set createdAt and updatedAt
    if (pb.getCreatedAt() == null) {
      pb.setCreatedAt(LocalDateTime.now());
    }
    pb.setUpdatedAt(LocalDateTime.now());

    // Calculate expected quantity from recipe
    if (pb.getExpectedQuantity() == null) {
      pb.setExpectedQuantity(recipe.getOutputQuantity());
    }

    // Save production batch first
    final ProductionBatch savedBatch = productionBatchRepository.save(pb);

    // Create material consumption records from recipe
    if (dto.getMaterialsConsumed() != null && !dto.getMaterialsConsumed().isEmpty()) {
      List<ProductionBatchMaterial> materials =
          dto.getMaterialsConsumed().stream()
              .map(
                  matDto -> {
                    ProductionBatchMaterial pbm = new ProductionBatchMaterial();
                    pbm.setProductionBatch(savedBatch);

                    Ingredient ingredient =
                        ingredientRepository
                            .findById(matDto.getIngredientId())
                            .orElseThrow(
                                () ->
                                    new RuntimeException(
                                        "Ingredient not found: " + matDto.getIngredientId()));
                    pbm.setIngredient(ingredient);

                    pbm.setQuantityRequired(matDto.getQuantityRequired());
                    pbm.setQuantityUsed(matDto.getQuantityUsed());
                    pbm.setUnit(matDto.getUnit());
                    pbm.setCostPerUnit(ingredient.getCostPerUnit());
                    pbm.setLotNumber(matDto.getLotNumber());
                    pbm.setNotes(matDto.getNotes());

                    return pbm;
                  })
              .collect(Collectors.toList());

      savedBatch.setMaterialsConsumed(materials);
    } else if (recipe.getIngredients() != null && !recipe.getIngredients().isEmpty()) {
      // Auto-populate from recipe ingredients
      List<ProductionBatchMaterial> materials =
          recipe.getIngredients().stream()
              .map(
                  ri -> {
                    ProductionBatchMaterial pbm = new ProductionBatchMaterial();
                    pbm.setProductionBatch(savedBatch);
                    pbm.setIngredient(ri.getIngredient());
                    pbm.setQuantityRequired(ri.getFinalQuantity());
                    pbm.setQuantityUsed(ri.getFinalQuantity()); // Default to required
                    pbm.setUnit(ri.getUnit());
                    pbm.setCostPerUnit(ri.getIngredient().getCostPerUnit());
                    return pbm;
                  })
              .collect(Collectors.toList());

      savedBatch.setMaterialsConsumed(materials);
    }

    // Calculate costs
    calculateProductionCosts(savedBatch);

    ProductionBatch finalSaved = productionBatchRepository.save(savedBatch);
    return toDto(finalSaved);
  }

  public ProductionBatchDto update(Long id, ProductionBatchDto dto) {
    ProductionBatch existing =
        productionBatchRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Production batch not found: " + id));

    // Cannot update if finalized
    if (existing.getIsFinalized()) {
      throw new RuntimeException("Cannot update finalized production batch");
    }

    existing.setProductionDate(dto.getProductionDate());
    existing.setBusinessLocation(dto.getBusinessLocation());
    existing.setQuantityProduced(dto.getQuantityProduced());
    existing.setWastedQuantity(dto.getWastedQuantity());
    existing.setStatus(dto.getStatus());
    existing.setLotNumber(dto.getLotNumber());
    existing.setAttachedDocumentPath(dto.getAttachedDocumentPath());
    existing.setNotes(dto.getNotes());
    existing.setUpdatedBy(dto.getUpdatedBy());
    // Set updatedAt
    existing.setUpdatedAt(LocalDateTime.now());

    // Update materials if provided
    if (dto.getMaterialsConsumed() != null) {
      existing.getMaterialsConsumed().clear();

      final ProductionBatch batchForLambda = existing;
      List<ProductionBatchMaterial> materials =
          dto.getMaterialsConsumed().stream()
              .map(
                  matDto -> {
                    ProductionBatchMaterial pbm = new ProductionBatchMaterial();
                    pbm.setProductionBatch(batchForLambda);

                    Ingredient ingredient =
                        ingredientRepository
                            .findById(matDto.getIngredientId())
                            .orElseThrow(
                                () ->
                                    new RuntimeException(
                                        "Ingredient not found: " + matDto.getIngredientId()));
                    pbm.setIngredient(ingredient);

                    pbm.setQuantityRequired(matDto.getQuantityRequired());
                    pbm.setQuantityUsed(matDto.getQuantityUsed());
                    pbm.setUnit(matDto.getUnit());
                    pbm.setCostPerUnit(ingredient.getCostPerUnit());
                    pbm.setLotNumber(matDto.getLotNumber());
                    pbm.setNotes(matDto.getNotes());

                    return pbm;
                  })
              .collect(Collectors.toList());

      existing.setMaterialsConsumed(materials);
    }

    // Recalculate costs
    calculateProductionCosts(existing);

    ProductionBatch saved = productionBatchRepository.save(existing);
    return toDto(saved);
  }

  public void delete(Long id) {
    ProductionBatch pb =
        productionBatchRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Production batch not found: " + id));

    if (pb.getIsFinalized()) {
      throw new RuntimeException("Cannot delete finalized production batch");
    }

    productionBatchRepository.deleteById(id);
  }

  public ProductionBatchDto getById(Long id) {
    return productionBatchRepository
        .findById(id)
        .map(this::toDto)
        .orElseThrow(() -> new NotFoundException("Production batch not found: " + id));
  }

  public List<ProductionBatchDto> listAll() {
    return productionBatchRepository.findAll().stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  // Paged version
  public Page<ProductionBatchDto> listAll(Pageable pageable) {
    Page<ProductionBatch> page = productionBatchRepository.findAll(pageable);
    List<ProductionBatchDto> dtos = page.stream().map(this::toDto).collect(Collectors.toList());
    return new PageImpl<>(dtos, pageable, page.getTotalElements());
  }

  public List<ProductionBatchDto> getByRecipe(Long recipeId) {
    return productionBatchRepository.findByRecipeId(recipeId).stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  // Paged version
  public Page<ProductionBatchDto> getByRecipe(Long recipeId, Pageable pageable) {
    Page<ProductionBatch> page = productionBatchRepository.findByRecipeId(recipeId, pageable);
    List<ProductionBatchDto> dtos = page.stream().map(this::toDto).collect(Collectors.toList());
    return new PageImpl<>(dtos, pageable, page.getTotalElements());
  }

  public List<ProductionBatchDto> getByStatus(String status) {
    return productionBatchRepository.findByStatus(status).stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  // Paged version
  public Page<ProductionBatchDto> getByStatus(String status, Pageable pageable) {
    Page<ProductionBatch> page = productionBatchRepository.findByStatus(status, pageable);
    List<ProductionBatchDto> dtos = page.stream().map(this::toDto).collect(Collectors.toList());
    return new PageImpl<>(dtos, pageable, page.getTotalElements());
  }

  public List<ProductionBatchDto> searchProductionBatches(
      String businessLocation,
      String status,
      Boolean isFinalized,
      Long recipeId,
      Long productId,
      LocalDateTime startDate,
      LocalDateTime endDate) {
    return productionBatchRepository
        .searchProductionBatches(
            businessLocation, status, isFinalized, recipeId, productId, startDate, endDate)
        .stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  // Paged version
  public Page<ProductionBatchDto> searchProductionBatches(
      String businessLocation,
      String status,
      Boolean isFinalized,
      Long recipeId,
      Long productId,
      LocalDateTime startDate,
      LocalDateTime endDate,
      Pageable pageable) {
    Page<ProductionBatch> page =
        productionBatchRepository.searchProductionBatches(
            businessLocation,
            status,
            isFinalized,
            recipeId,
            productId,
            startDate,
            endDate,
            pageable);
    List<ProductionBatchDto> dtos = page.stream().map(this::toDto).collect(Collectors.toList());
    return new PageImpl<>(dtos, pageable, page.getTotalElements());
  }

  public List<String> getAllBusinessLocations() {
    return productionBatchRepository.findAllBusinessLocations();
  }

  /** Finalize production batch - locks it from further edits and updates inventory */
  public ProductionBatchDto finalize(Long id, String finalizedBy) {
    ProductionBatch pb =
        productionBatchRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Production batch not found: " + id));

    if (pb.getIsFinalized()) {
      throw new RuntimeException("Production batch is already finalized");
    }

    // Validate completion
    if (!"COMPLETED".equals(pb.getStatus())) {
      throw new RuntimeException("Only completed batches can be finalized");
    }

    // Update inventory - deduct ingredients, add finished product
    if (pb.getMaterialsConsumed() != null) {
      pb.getMaterialsConsumed()
          .forEach(
              material -> {
                Ingredient ingredient = material.getIngredient();
                BigDecimal newStock =
                    ingredient.getCurrentStock().subtract(material.getQuantityUsed());
                ingredient.setCurrentStock(newStock);
                ingredientRepository.save(ingredient);
              });
    }

    // Add finished product to inventory if applicable
    if (pb.getProduct() != null) {
      Product product = pb.getProduct();
      // Update product stock (if needed)
      productRepository.save(product);
    }

    pb.setIsFinalized(true);
    pb.setFinalizedAt(LocalDateTime.now());
    pb.setFinalizedBy(finalizedBy);
    pb.setStatus("FINALIZED");

    ProductionBatch saved = productionBatchRepository.save(pb);
    return toDto(saved);
  }

  /** Unfinalize production batch - reverses finalization */
  public ProductionBatchDto unfinalize(Long id) {
    ProductionBatch pb =
        productionBatchRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Production batch not found: " + id));

    if (!pb.getIsFinalized()) {
      throw new RuntimeException("Production batch is not finalized");
    }

    // Reverse inventory changes
    if (pb.getMaterialsConsumed() != null) {
      pb.getMaterialsConsumed()
          .forEach(
              material -> {
                Ingredient ingredient = material.getIngredient();
                BigDecimal newStock = ingredient.getCurrentStock().add(material.getQuantityUsed());
                ingredient.setCurrentStock(newStock);
                ingredientRepository.save(ingredient);
              });
    }

    pb.setIsFinalized(false);
    pb.setFinalizedAt(null);
    pb.setFinalizedBy(null);
    pb.setStatus("COMPLETED");

    ProductionBatch saved = productionBatchRepository.save(pb);
    return toDto(saved);
  }

  /** Calculate production costs */
  private void calculateProductionCosts(ProductionBatch pb) {
    Recipe recipe = pb.getRecipe();

    // Calculate ingredient cost from materials consumed
    BigDecimal ingredientCost = BigDecimal.ZERO;
    if (pb.getMaterialsConsumed() != null) {
      ingredientCost =
          pb.getMaterialsConsumed().stream()
              .map(ProductionBatchMaterial::getTotalCost)
              .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    pb.setIngredientCost(ingredientCost);

    // Production cost (fixed + variable)
    BigDecimal productionCost =
        (recipe.getFixedProductionCost() != null
                ? recipe.getFixedProductionCost()
                : BigDecimal.ZERO)
            .add(
                recipe.getVariableProductionCost() != null
                    ? recipe.getVariableProductionCost()
                    : BigDecimal.ZERO);
    pb.setProductionCost(productionCost);

    // Total cost
    pb.setTotalCost(ingredientCost.add(productionCost));
  }

  /** Generate unique reference number in format: DDMMYYYY/NNNN */
  private String generateReferenceNumber() {
    String datePrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));

    // Find the last reference number for today
    List<ProductionBatch> todayBatches =
        productionBatchRepository.findRecentBatches(
            LocalDateTime.now().withHour(0).withMinute(0).withSecond(0));

    int nextNumber = 1;
    if (!todayBatches.isEmpty()) {
      // Extract the highest number from today's batches
      nextNumber =
          todayBatches.stream()
                  .map(pb -> pb.getReferenceNumber())
                  .filter(ref -> ref.startsWith(datePrefix))
                  .map(
                      ref -> {
                        try {
                          return Integer.parseInt(ref.substring(ref.lastIndexOf("/") + 1));
                        } catch (Exception e) {
                          return 0;
                        }
                      })
                  .max(Integer::compare)
                  .orElse(0)
              + 1;
    }

    return String.format("%s/%04d", datePrefix, nextNumber);
  }
}
