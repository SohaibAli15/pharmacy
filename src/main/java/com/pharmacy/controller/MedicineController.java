package com.pharmacy.controller;

import com.pharmacy.dto.MedicineDto;
import com.pharmacy.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @GetMapping
    public List<MedicineDto> listAll() {
        return medicineService.listAll();
    }

    @GetMapping("/search")
    public List<MedicineDto> search(@RequestParam("q") String q) {
        return medicineService.searchByName(q);
    }

    @GetMapping("/category/{category}")
    public List<MedicineDto> byCategory(@PathVariable String category) {
        return medicineService.findByCategory(category);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineDto> get(@PathVariable Long id) {
        MedicineDto dto = medicineService.getById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<MedicineDto> create(@RequestBody MedicineDto dto) {
        MedicineDto created = medicineService.create(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineDto> update(@PathVariable Long id, @RequestBody MedicineDto dto) {
        MedicineDto updated = medicineService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        medicineService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
