package com.pharmacy.controller;

import com.pharmacy.entity.Prescription;
import com.pharmacy.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping
    public ResponseEntity<Prescription> createPrescription(@RequestBody Prescription prescription) {
        Prescription savedPrescription = prescriptionService.savePrescription(prescription);
        return ResponseEntity.ok(savedPrescription);
    }

    @GetMapping
    public ResponseEntity<List<Prescription>> getAllPrescriptions() {
        List<Prescription> prescriptions = prescriptionService.findByStatus("APPROVED"); // or all
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable Long id) {
        return prescriptionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByStatus(@PathVariable String status) {
        List<Prescription> prescriptions = prescriptionService.findByStatus(status);
        return ResponseEntity.ok(prescriptions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }
}
