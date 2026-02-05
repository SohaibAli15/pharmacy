/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
  Optional<Account> findByAccountCode(String accountCode);

  Optional<Account> findByAccountName(String accountName);

  List<Account> findByAccountType(Account.AccountType accountType);

  List<Account> findByStatus(Account.AccountStatus status);

  // Find account by party
  @Query(
      "SELECT a FROM Account a WHERE a.id IN (SELECT p.account.id FROM Party p WHERE p.id = :partyId)")
  Optional<Account> findByPartyId(@Param("partyId") Long partyId);
}
