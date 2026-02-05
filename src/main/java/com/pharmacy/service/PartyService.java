/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service;

import java.util.List;

import com.pharmacy.dto.PartyDto;
import com.pharmacy.entity.Party;

public interface PartyService {
  /** Create a new party (customer/supplier or both) */
  PartyDto createParty(PartyDto partyDto);

  /** Get party by ID */
  PartyDto getPartyById(Long partyId);

  /** Get party by party code */
  PartyDto getPartyByCode(String partyCode);

  /** Get party by email */
  PartyDto getPartyByEmail(String email);

  /** Get all parties */
  List<PartyDto> getAllParties();

  /** Get parties by type */
  List<PartyDto> getPartiesByType(Party.PartyType partyType);

  /** Update party information */
  PartyDto updateParty(Long partyId, PartyDto partyDto);

  /** Delete party */
  void deleteParty(Long partyId);

  /** Link customer to party */
  PartyDto linkCustomerToParty(Long partyId, Long customerId);

  /** Link supplier to party */
  PartyDto linkSupplierToParty(Long partyId, Long supplierId);

  /** Get party by customer ID */
  PartyDto getPartyByCustomerId(Long customerId);

  /** Get party by supplier ID */
  PartyDto getPartyBySupplierId(Long supplierId);
}
