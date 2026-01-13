package com.pharmacy.service;

import com.pharmacy.entity.Sale;
import com.pharmacy.entity.User;
import com.pharmacy.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;

    public Sale saveSale(Sale sale) {
        return saleRepository.save(sale);
    }

    public Optional<Sale> findById(Long id) {
        return saleRepository.findById(id);
    }

    public List<Sale> findByCustomer(User customer) {
        return saleRepository.findByCustomer(customer);
    }

    public List<Sale> findByPharmacist(User pharmacist) {
        return saleRepository.findByPharmacist(pharmacist);
    }

    public List<Sale> findSalesBetweenDates(LocalDateTime start, LocalDateTime end) {
        return saleRepository.findBySaleDateBetween(start, end);
    }

    public void deleteSale(Long id) {
        saleRepository.deleteById(id);
    }
}
