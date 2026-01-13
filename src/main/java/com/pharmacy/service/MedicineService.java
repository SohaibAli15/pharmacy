package com.pharmacy.service;

import com.pharmacy.dto.MedicineDto;
import com.pharmacy.entity.Medicine;
import com.pharmacy.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicineService {

    private final MedicineRepository medicineRepository;

    public MedicineDto toDto(Medicine m) {
        MedicineDto dto = new MedicineDto();
        dto.setId(m.getId());
        dto.setName(m.getName());
        dto.setDescription(m.getDescription());
        dto.setManufacturer(m.getManufacturer());
        dto.setPrice(m.getPrice());
        dto.setStockQuantity(m.getStockQuantity());
        dto.setExpiryDate(m.getExpiryDate());
        dto.setCategory(m.getCategory());
        return dto;
    }

    public Medicine fromDto(MedicineDto dto) {
        Medicine m = new Medicine();
        m.setId(dto.getId());
        m.setName(dto.getName());
        m.setDescription(dto.getDescription());
        m.setManufacturer(dto.getManufacturer());
        m.setPrice(dto.getPrice());
        m.setStockQuantity(dto.getStockQuantity());
        m.setExpiryDate(dto.getExpiryDate());
        m.setCategory(dto.getCategory());
        return m;
    }

    public MedicineDto create(MedicineDto dto) {
        Medicine m = fromDto(dto);
        Medicine saved = medicineRepository.save(m);
        return toDto(saved);
    }

    public MedicineDto update(Long id, MedicineDto dto) {
        Medicine existing = medicineRepository.findById(id).orElseThrow();
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setManufacturer(dto.getManufacturer());
        existing.setPrice(dto.getPrice());
        existing.setStockQuantity(dto.getStockQuantity());
        existing.setExpiryDate(dto.getExpiryDate());
        existing.setCategory(dto.getCategory());
        Medicine saved = medicineRepository.save(existing);
        return toDto(saved);
    }

    public void delete(Long id) {
        medicineRepository.deleteById(id);
    }

    public MedicineDto getById(Long id) {
        return medicineRepository.findById(id).map(this::toDto).orElse(null);
    }

    public List<MedicineDto> searchByName(String name) {
        return medicineRepository.findByNameContainingIgnoreCase(name).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<MedicineDto> findByCategory(String category) {
        return medicineRepository.findByCategory(category).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<MedicineDto> listAll() {
        return medicineRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }
}
