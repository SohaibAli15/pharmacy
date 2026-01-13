package com.pharmacy.service;

import com.pharmacy.dto.IngredientDto;
import com.pharmacy.entity.Ingredient;
import com.pharmacy.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientDto toDto(Ingredient i) {
        IngredientDto dto = new IngredientDto();
        dto.setId(i.getId());
        dto.setName(i.getName());
        dto.setDescription(i.getDescription());
        dto.setUnit(i.getUnit());
        dto.setCurrentStock(i.getCurrentStock());
        dto.setThresholdLow(i.getThresholdLow());
        dto.setThresholdHigh(i.getThresholdHigh());
        dto.setCostPerUnit(i.getCostPerUnit());
        dto.setCreatedAt(i.getCreatedAt());
        dto.setUpdatedAt(i.getUpdatedAt());
        return dto;
    }

    public Ingredient fromDto(IngredientDto dto) {
        Ingredient i = new Ingredient();
        i.setId(dto.getId());
        i.setName(dto.getName());
        i.setDescription(dto.getDescription());
        i.setUnit(dto.getUnit());
        i.setCurrentStock(dto.getCurrentStock());
        i.setThresholdLow(dto.getThresholdLow());
        i.setThresholdHigh(dto.getThresholdHigh());
        i.setCostPerUnit(dto.getCostPerUnit());
        i.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now());
        i.setUpdatedAt(LocalDateTime.now());
        return i;
    }

    public IngredientDto create(IngredientDto dto) {
        Ingredient i = fromDto(dto);
        Ingredient saved = ingredientRepository.save(i);
        return toDto(saved);
    }

    public IngredientDto update(Long id, IngredientDto dto) {
        Ingredient existing = ingredientRepository.findById(id).orElseThrow();
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setUnit(dto.getUnit());
        existing.setCurrentStock(dto.getCurrentStock());
        existing.setThresholdLow(dto.getThresholdLow());
        existing.setThresholdHigh(dto.getThresholdHigh());
        existing.setCostPerUnit(dto.getCostPerUnit());
        existing.setUpdatedAt(LocalDateTime.now());
        Ingredient saved = ingredientRepository.save(existing);
        return toDto(saved);
    }

    public void delete(Long id) {
        ingredientRepository.deleteById(id);
    }

    public IngredientDto getById(Long id) {
        return ingredientRepository.findById(id).map(this::toDto).orElse(null);
    }

    public List<IngredientDto> listAll() {
        return ingredientRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<IngredientDto> getLowStockIngredients() {
        return ingredientRepository.findAll().stream()
                .filter(i -> i.getCurrentStock().compareTo(i.getThresholdLow()) < 0)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<IngredientDto> getHighStockIngredients() {
        return ingredientRepository.findAll().stream()
                .filter(i -> i.getCurrentStock().compareTo(i.getThresholdHigh()) > 0)
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
