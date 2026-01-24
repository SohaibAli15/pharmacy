/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmacy.repository.CategoryRepository;
import com.pharmacy.repository.IngredientRepository;
import com.pharmacy.repository.MedicineRepository;
import com.pharmacy.repository.StoreRepository;
import com.pharmacy.repository.SubCategoryRepository;
import com.pharmacy.repository.SupplierRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/lookup")
@RequiredArgsConstructor
public class LookupController {
  private final MedicineRepository medicineRepository;
  private final StoreRepository storeRepository;
  private final SupplierRepository supplierRepository;
  private final IngredientRepository ingredientRepository;
  private final CategoryRepository categoryRepository;
  private final SubCategoryRepository subCategoryRepository;

  @GetMapping("")
  public Map<String, Object> getAllLookups() {
    Map<String, Object> result = new HashMap<>();
    result.put("medicines", medicineRepository.findAll());
    result.put("stores", storeRepository.findAll());
    result.put("suppliers", supplierRepository.findAll());
    result.put("ingredients", ingredientRepository.findAll());
    result.put("categories", categoryRepository.findAll());
    result.put("subcategories", subCategoryRepository.findAll());
    return result;
  }
}
