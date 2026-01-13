package com.pharmacy.repository;

import com.pharmacy.entity.Sale;
import com.pharmacy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByCustomer(User customer);
    List<Sale> findByPharmacist(User pharmacist);
    List<Sale> findBySaleDateBetween(LocalDateTime start, LocalDateTime end);
}
