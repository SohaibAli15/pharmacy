/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.Party;

@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {
  Optional<Party> findByPartyCode(String partyCode);

  Optional<Party> findByEmail(String email);

  Optional<Party> findByCustomerId(Long customerId);

  Optional<Party> findBySupplierId(Long supplierId);

  Optional<Party> findByPartyType(Party.PartyType partyType);
}
