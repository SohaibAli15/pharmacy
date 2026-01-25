/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacy.entity.MaterialIssue;

@Repository
public interface MaterialIssueRepository extends JpaRepository<MaterialIssue, Long> {}
