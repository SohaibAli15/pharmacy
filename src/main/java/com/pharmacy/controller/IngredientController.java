package com.pharmacy.controller;

import com.pharmacy.dto.IngredientDto;
import com.pharmacy.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping
    public List<IngredientDto> listAll() {
        return ingredientService.listAll();
    }

    @GetMapping("/low-stock")
    public List<IngredientDto> getLowStock() {
        return ingredientService.getLowStockIngredients();
    }

    @GetMapping("/high-stock")
    public List<IngredientDto> getHighStock() {
        return ingredientService.getHighStockIngredients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDto> get(@PathVariable Long id) {
        IngredientDto dto = ingredientService.getById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<IngredientDto> create(@RequestBody IngredientDto dto) {
        IngredientDto created = ingredientService.create(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientDto> update(@PathVariable Long id, @RequestBody IngredientDto dto) {
        IngredientDto updated = ingredientService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ingredientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
