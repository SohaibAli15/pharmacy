/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.ProductDto;
import com.pharmacy.dto.ProductPageResponse;
import com.pharmacy.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Product Management", description = "APIs for managing finished products (v1)")
public class ProductController {

  private final ProductService productService;

  @GetMapping
  @Operation(
      summary = "Get all products",
      description = "Retrieve all products in the catalog with pagination")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved products",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ProductPageResponse.class)))
  public ResponseEntity<ProductPageResponse> getAllProducts(
      @PageableDefault(size = 20) Pageable pageable) {
    Page<ProductDto> products = productService.listAll(pageable);
    ProductPageResponse response = new ProductPageResponse();
    response.setContent(products.getContent());
    response.setTotalElements(products.getTotalElements());
    response.setTotalPages(products.getTotalPages());
    response.setNumber(products.getNumber());
    response.setSize(products.getSize());
    response.setFirst(products.isFirst());
    response.setLast(products.isLast());
    response.setEmpty(products.isEmpty());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/search")
  @Operation(summary = "Search products", description = "Search products by name with pagination")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved matching products",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ProductPageResponse.class)))
  public ResponseEntity<ProductPageResponse> searchProducts(
      @Parameter(description = "Search query", required = true) @RequestParam("q") String q,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<ProductDto> products = productService.searchByName(q, pageable);
    ProductPageResponse response = new ProductPageResponse();
    response.setContent(products.getContent());
    response.setTotalElements(products.getTotalElements());
    response.setTotalPages(products.getTotalPages());
    response.setNumber(products.getNumber());
    response.setSize(products.getSize());
    response.setFirst(products.isFirst());
    response.setLast(products.isLast());
    response.setEmpty(products.isEmpty());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/category/{category}")
  @Operation(
      summary = "Get products by category",
      description = "Retrieve products filtered by category with pagination")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved products by category",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ProductPageResponse.class)))
  public ResponseEntity<ProductPageResponse> getProductsByCategory(
      @Parameter(description = "Product category", required = true) @PathVariable String category,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<ProductDto> products = productService.findByCategory(category, pageable);
    ProductPageResponse response = new ProductPageResponse();
    response.setContent(products.getContent());
    response.setTotalElements(products.getTotalElements());
    response.setTotalPages(products.getTotalPages());
    response.setNumber(products.getNumber());
    response.setSize(products.getSize());
    response.setFirst(products.isFirst());
    response.setLast(products.isLast());
    response.setEmpty(products.isEmpty());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Product found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductDto.class))),
        @ApiResponse(responseCode = "404", description = "Product not found")
      })
  public ResponseEntity<ProductDto> getProductById(
      @Parameter(description = "Product ID", required = true) @PathVariable Long id) {
    ProductDto dto = productService.getById(id);
    if (dto == null) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(dto);
  }

  @PostMapping
  @Operation(
      summary = "Create a new product",
      description = "Register a new product with specifications and pricing")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Product created successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
      })
  public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto dto) {
    ProductDto created = productService.create(dto);
    return ResponseEntity.ok(created);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update a product",
      description = "Update product information including specifications and pricing")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Product updated successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductDto.class))),
        @ApiResponse(responseCode = "404", description = "Product not found")
      })
  public ResponseEntity<ProductDto> updateProduct(
      @Parameter(description = "Product ID", required = true) @PathVariable Long id,
      @RequestBody ProductDto dto) {
    ProductDto updated = productService.update(id, dto);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a product", description = "Remove a product from the catalog")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
      })
  public ResponseEntity<Void> deleteProduct(
      @Parameter(description = "Product ID", required = true) @PathVariable Long id) {
    productService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
