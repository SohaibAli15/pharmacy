package com.pharmacy.service;

import com.pharmacy.entity.Prescription;
import com.pharmacy.entity.User;
import com.pharmacy.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public Prescription savePrescription(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    public Optional<Prescription> findById(Long id) {
        return prescriptionRepository.findById(id);
    }

    public List<Prescription> findByCustomer(User customer) {
        return prescriptionRepository.findByCustomer(customer);
    }

    public List<Prescription> findByPharmacist(User pharmacist) {
        return prescriptionRepository.findByPharmacist(pharmacist);
    }

    public List<Prescription> findByStatus(String status) {
        return prescriptionRepository.findByStatus(status);
    }

    public void deletePrescription(Long id) {
        prescriptionRepository.deleteById(id);
    }
}
