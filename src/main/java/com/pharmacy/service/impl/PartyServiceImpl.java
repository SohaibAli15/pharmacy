/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmacy.dto.PartyDto;
import com.pharmacy.entity.Account;
import com.pharmacy.entity.Customer;
import com.pharmacy.entity.Party;
import com.pharmacy.entity.Supplier;
import com.pharmacy.repository.AccountRepository;
import com.pharmacy.repository.CustomerRepository;
import com.pharmacy.repository.PartyRepository;
import com.pharmacy.repository.SupplierRepository;
import com.pharmacy.service.PartyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartyServiceImpl implements PartyService {

  private final PartyRepository partyRepository;
  private final AccountRepository accountRepository;
  private final CustomerRepository customerRepository;
  private final SupplierRepository supplierRepository;

  @Override
  @Transactional
  public PartyDto createParty(PartyDto partyDto) {
    log.info("Creating party: {}", partyDto.getPartyCode());

    // Create the Party entity
    Party party = new Party();
    party.setPartyCode(partyDto.getPartyCode());
    party.setPartyName(partyDto.getPartyName());
    party.setPartyType(partyDto.getPartyType());
    party.setEmail(partyDto.getEmail());
    party.setPhone(partyDto.getPhone());
    party.setAddress(partyDto.getAddress());
    party.setCity(partyDto.getCity());
    party.setState(partyDto.getState());
    party.setCountry(partyDto.getCountry());
    party.setZipCode(partyDto.getZipCode());
    party.setTaxId(partyDto.getTaxId());
    party.setBankAccount(partyDto.getBankAccount());
    party.setNotes(partyDto.getNotes());

    // Create the associated Account
    Account account = new Account();
    account.setAccountCode(partyDto.getPartyCode()); // Use party code as account code
    account.setAccountName(partyDto.getPartyName());

    // Set account type based on party type
    if (partyDto.getPartyType() == Party.PartyType.CUSTOMER_ONLY) {
      account.setAccountType(Account.AccountType.RECEIVABLE);
    } else if (partyDto.getPartyType() == Party.PartyType.SUPPLIER_ONLY) {
      account.setAccountType(Account.AccountType.PAYABLE);
    } else {
      account.setAccountType(Account.AccountType.BOTH);
    }

    account.setAccountOpeningDate(LocalDate.now());
    account.setStatus(Account.AccountStatus.ACTIVE);
    Account savedAccount = accountRepository.save(account);

    // Link account to party
    party.setAccount(savedAccount);

    Party savedParty = partyRepository.save(party);
    log.info("Party created with linked account: {}", savedParty.getId());

    return mapToDto(savedParty);
  }

  @Override
  @Transactional(readOnly = true)
  public PartyDto getPartyById(Long partyId) {
    log.debug("Fetching party: {}", partyId);
    Party party =
        partyRepository
            .findById(partyId)
            .orElseThrow(() -> new RuntimeException("Party not found: " + partyId));
    return mapToDto(party);
  }

  @Override
  @Transactional(readOnly = true)
  public PartyDto getPartyByCode(String partyCode) {
    log.debug("Fetching party by code: {}", partyCode);
    Party party =
        partyRepository
            .findByPartyCode(partyCode)
            .orElseThrow(() -> new RuntimeException("Party not found: " + partyCode));
    return mapToDto(party);
  }

  @Override
  @Transactional(readOnly = true)
  public PartyDto getPartyByEmail(String email) {
    log.debug("Fetching party by email: {}", email);
    Party party =
        partyRepository
            .findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Party not found: " + email));
    return mapToDto(party);
  }

  @Override
  @Transactional(readOnly = true)
  public List<PartyDto> getAllParties() {
    log.debug("Fetching all parties");
    return partyRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<PartyDto> getPartiesByType(Party.PartyType partyType) {
    log.debug("Fetching parties by type: {}", partyType);
    return partyRepository.findAll().stream()
        .filter(p -> p.getPartyType() == partyType)
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public PartyDto updateParty(Long partyId, PartyDto partyDto) {
    log.info("Updating party: {}", partyId);

    Party party =
        partyRepository
            .findById(partyId)
            .orElseThrow(() -> new RuntimeException("Party not found: " + partyId));

    party.setPartyName(partyDto.getPartyName());
    party.setEmail(partyDto.getEmail());
    party.setPhone(partyDto.getPhone());
    party.setAddress(partyDto.getAddress());
    party.setCity(partyDto.getCity());
    party.setState(partyDto.getState());
    party.setCountry(partyDto.getCountry());
    party.setZipCode(partyDto.getZipCode());
    party.setTaxId(partyDto.getTaxId());
    party.setBankAccount(partyDto.getBankAccount());
    party.setNotes(partyDto.getNotes());
    party.setUpdatedAt(LocalDateTime.now());

    Party updatedParty = partyRepository.save(party);
    log.info("Party updated successfully");

    return mapToDto(updatedParty);
  }

  @Override
  @Transactional
  public void deleteParty(Long partyId) {
    log.info("Deleting party: {}", partyId);

    Party party =
        partyRepository
            .findById(partyId)
            .orElseThrow(() -> new RuntimeException("Party not found: " + partyId));

    partyRepository.delete(party);
    log.info("Party deleted successfully");
  }

  @Override
  @Transactional
  public PartyDto linkCustomerToParty(Long partyId, Long customerId) {
    log.info("Linking customer {} to party {}", customerId, partyId);

    Party party =
        partyRepository
            .findById(partyId)
            .orElseThrow(() -> new RuntimeException("Party not found: " + partyId));

    Customer customer =
        customerRepository
            .findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));

    party.setCustomer(customer);
    customer.setParty(party);

    // Update party type if needed
    if (party.getPartyType() == Party.PartyType.SUPPLIER_ONLY) {
      party.setPartyType(Party.PartyType.BOTH);
      party.getAccount().setAccountType(Account.AccountType.BOTH);
    } else if (party.getPartyType() == null) {
      party.setPartyType(Party.PartyType.CUSTOMER_ONLY);
      party.getAccount().setAccountType(Account.AccountType.RECEIVABLE);
    }

    party.setUpdatedAt(LocalDateTime.now());
    Party updatedParty = partyRepository.save(party);

    log.info("Customer linked to party successfully");
    return mapToDto(updatedParty);
  }

  @Override
  @Transactional
  public PartyDto linkSupplierToParty(Long partyId, Long supplierId) {
    log.info("Linking supplier {} to party {}", supplierId, partyId);

    Party party =
        partyRepository
            .findById(partyId)
            .orElseThrow(() -> new RuntimeException("Party not found: " + partyId));

    Supplier supplier =
        supplierRepository
            .findById(supplierId)
            .orElseThrow(() -> new RuntimeException("Supplier not found: " + supplierId));

    party.setSupplier(supplier);
    supplier.setParty(party);

    // Update party type if needed
    if (party.getPartyType() == Party.PartyType.CUSTOMER_ONLY) {
      party.setPartyType(Party.PartyType.BOTH);
      party.getAccount().setAccountType(Account.AccountType.BOTH);
    } else if (party.getPartyType() == null) {
      party.setPartyType(Party.PartyType.SUPPLIER_ONLY);
      party.getAccount().setAccountType(Account.AccountType.PAYABLE);
    }

    party.setUpdatedAt(LocalDateTime.now());
    Party updatedParty = partyRepository.save(party);

    log.info("Supplier linked to party successfully");
    return mapToDto(updatedParty);
  }

  @Override
  @Transactional(readOnly = true)
  public PartyDto getPartyByCustomerId(Long customerId) {
    log.debug("Fetching party by customer: {}", customerId);
    Party party =
        partyRepository
            .findByCustomerId(customerId)
            .orElseThrow(() -> new RuntimeException("Party not found for customer: " + customerId));
    return mapToDto(party);
  }

  @Override
  @Transactional(readOnly = true)
  public PartyDto getPartyBySupplierId(Long supplierId) {
    log.debug("Fetching party by supplier: {}", supplierId);
    Party party =
        partyRepository
            .findBySupplierId(supplierId)
            .orElseThrow(() -> new RuntimeException("Party not found for supplier: " + supplierId));
    return mapToDto(party);
  }

  private PartyDto mapToDto(Party party) {
    PartyDto dto = new PartyDto();
    dto.setId(party.getId());
    dto.setPartyCode(party.getPartyCode());
    dto.setPartyName(party.getPartyName());
    dto.setPartyType(party.getPartyType());
    dto.setEmail(party.getEmail());
    dto.setPhone(party.getPhone());
    dto.setAddress(party.getAddress());
    dto.setCity(party.getCity());
    dto.setState(party.getState());
    dto.setCountry(party.getCountry());
    dto.setZipCode(party.getZipCode());
    dto.setTaxId(party.getTaxId());
    dto.setBankAccount(party.getBankAccount());
    dto.setNotes(party.getNotes());
    dto.setCustomerId(party.getCustomer() != null ? party.getCustomer().getId() : null);
    dto.setSupplierId(party.getSupplier() != null ? party.getSupplier().getId() : null);
    dto.setAccountId(party.getAccount() != null ? party.getAccount().getId() : null);
    dto.setCreatedAt(party.getCreatedAt());
    dto.setUpdatedAt(party.getUpdatedAt());
    return dto;
  }
}
