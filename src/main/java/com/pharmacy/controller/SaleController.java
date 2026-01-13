package com.pharmacy.controller;

import com.pharmacy.entity.Sale;
import com.pharmacy.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody Sale sale) {
        Sale savedSale = saleService.saveSale(sale);
        return ResponseEntity.ok(savedSale);
    }

    @GetMapping
    public ResponseEntity<List<Sale>> getAllSales() {
        List<Sale> sales = saleService.findByPharmacist(null); // adjust
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        return saleService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Sale>> getSalesBetweenDates(
            @RequestParam String start,
            @RequestParam String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        List<Sale> sales = saleService.findSalesBetweenDates(startDate, endDate);
        return ResponseEntity.ok(sales);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }
}
