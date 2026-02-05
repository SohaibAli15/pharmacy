/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pharmacy.dto.PartyDto;
import com.pharmacy.entity.Party;
import com.pharmacy.service.PartyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/parties")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Party Management", description = "APIs for managing parties (customers/suppliers)")
public class PartyController {

  private final PartyService partyService;

  @PostMapping
  @Operation(
      summary = "Create a new party",
      description = "Create a new party (customer, supplier, or both) with linked account")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Party created successfully",
            content = @Content(schema = @Schema(implementation = PartyDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
      })
  public ResponseEntity<PartyDto> createParty(@RequestBody PartyDto partyDto) {
    PartyDto created = partyService.createParty(partyDto);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get party by ID", description = "Retrieve a specific party by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Party found",
            content = @Content(schema = @Schema(implementation = PartyDto.class))),
        @ApiResponse(responseCode = "404", description = "Party not found")
      })
  public ResponseEntity<PartyDto> getPartyById(
      @Parameter(description = "Party ID", required = true) @PathVariable Long id) {
    PartyDto party = partyService.getPartyById(id);
    return ResponseEntity.ok(party);
  }

  @GetMapping("/code/{code}")
  @Operation(summary = "Get party by code", description = "Retrieve a party by its code")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Party found",
            content = @Content(schema = @Schema(implementation = PartyDto.class))),
        @ApiResponse(responseCode = "404", description = "Party not found")
      })
  public ResponseEntity<PartyDto> getPartyByCode(
      @Parameter(description = "Party code", required = true) @PathVariable String code) {
    PartyDto party = partyService.getPartyByCode(code);
    return ResponseEntity.ok(party);
  }

  @GetMapping("/email/{email}")
  @Operation(summary = "Get party by email", description = "Retrieve a party by its email")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Party found",
            content = @Content(schema = @Schema(implementation = PartyDto.class))),
        @ApiResponse(responseCode = "404", description = "Party not found")
      })
  public ResponseEntity<PartyDto> getPartyByEmail(
      @Parameter(description = "Party email", required = true) @PathVariable String email) {
    PartyDto party = partyService.getPartyByEmail(email);
    return ResponseEntity.ok(party);
  }

  @GetMapping
  @Operation(summary = "Get all parties", description = "Retrieve all parties")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved parties",
      content = @Content(schema = @Schema(implementation = PartyDto.class)))
  public ResponseEntity<List<PartyDto>> getAllParties() {
    List<PartyDto> parties = partyService.getAllParties();
    return ResponseEntity.ok(parties);
  }

  @GetMapping("/type/{type}")
  @Operation(
      summary = "Get parties by type",
      description = "Retrieve parties filtered by type (CUSTOMER_ONLY, SUPPLIER_ONLY, BOTH)")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved parties",
      content = @Content(schema = @Schema(implementation = PartyDto.class)))
  public ResponseEntity<List<PartyDto>> getPartiesByType(
      @Parameter(description = "Party type", required = true) @PathVariable Party.PartyType type) {
    List<PartyDto> parties = partyService.getPartiesByType(type);
    return ResponseEntity.ok(parties);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update party", description = "Update party information")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Party updated successfully",
            content = @Content(schema = @Schema(implementation = PartyDto.class))),
        @ApiResponse(responseCode = "404", description = "Party not found")
      })
  public ResponseEntity<PartyDto> updateParty(
      @Parameter(description = "Party ID", required = true) @PathVariable Long id,
      @RequestBody PartyDto partyDto) {
    PartyDto updated = partyService.updateParty(id, partyDto);
    return ResponseEntity.ok(updated);
  }

  @PostMapping("/{partyId}/link-customer/{customerId}")
  @Operation(
      summary = "Link customer to party",
      description = "Link an existing customer to a party")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Customer linked successfully",
            content = @Content(schema = @Schema(implementation = PartyDto.class))),
        @ApiResponse(responseCode = "404", description = "Party or customer not found")
      })
  public ResponseEntity<PartyDto> linkCustomerToParty(
      @Parameter(description = "Party ID", required = true) @PathVariable Long partyId,
      @Parameter(description = "Customer ID", required = true) @PathVariable Long customerId) {
    PartyDto updated = partyService.linkCustomerToParty(partyId, customerId);
    return ResponseEntity.ok(updated);
  }

  @PostMapping("/{partyId}/link-supplier/{supplierId}")
  @Operation(
      summary = "Link supplier to party",
      description = "Link an existing supplier to a party")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Supplier linked successfully",
            content = @Content(schema = @Schema(implementation = PartyDto.class))),
        @ApiResponse(responseCode = "404", description = "Party or supplier not found")
      })
  public ResponseEntity<PartyDto> linkSupplierToParty(
      @Parameter(description = "Party ID", required = true) @PathVariable Long partyId,
      @Parameter(description = "Supplier ID", required = true) @PathVariable Long supplierId) {
    PartyDto updated = partyService.linkSupplierToParty(partyId, supplierId);
    return ResponseEntity.ok(updated);
  }

  @GetMapping("/customer/{customerId}")
  @Operation(
      summary = "Get party by customer ID",
      description = "Retrieve party associated with a customer")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Party found",
            content = @Content(schema = @Schema(implementation = PartyDto.class))),
        @ApiResponse(responseCode = "404", description = "Party not found")
      })
  public ResponseEntity<PartyDto> getPartyByCustomerId(
      @Parameter(description = "Customer ID", required = true) @PathVariable Long customerId) {
    PartyDto party = partyService.getPartyByCustomerId(customerId);
    return ResponseEntity.ok(party);
  }

  @GetMapping("/supplier/{supplierId}")
  @Operation(
      summary = "Get party by supplier ID",
      description = "Retrieve party associated with a supplier")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Party found",
            content = @Content(schema = @Schema(implementation = PartyDto.class))),
        @ApiResponse(responseCode = "404", description = "Party not found")
      })
  public ResponseEntity<PartyDto> getPartyBySupplierId(
      @Parameter(description = "Supplier ID", required = true) @PathVariable Long supplierId) {
    PartyDto party = partyService.getPartyBySupplierId(supplierId);
    return ResponseEntity.ok(party);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete party", description = "Delete a party by ID")
  @ApiResponse(responseCode = "204", description = "Party deleted")
  public ResponseEntity<Void> deleteParty(
      @Parameter(description = "Party ID", required = true) @PathVariable Long id) {
    partyService.deleteParty(id);
    return ResponseEntity.noContent().build();
  }
}
