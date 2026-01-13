package com.pharmacy.repository;

import com.pharmacy.entity.Prescription;
import com.pharmacy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByCustomer(User customer);
    List<Prescription> findByPharmacist(User pharmacist);
    List<Prescription> findByStatus(String status);
}
