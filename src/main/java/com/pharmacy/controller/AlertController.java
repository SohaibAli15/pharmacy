package com.pharmacy.controller;

import com.pharmacy.dto.AlertDto;
import com.pharmacy.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping
    public List<AlertDto> listAll() {
        return alertService.listAll();
    }

    @GetMapping("/unread")
    public List<AlertDto> getUnread() {
        return alertService.getUnreadAlerts();
    }

    @GetMapping("/ingredient/{ingredientId}")
    public List<AlertDto> getByIngredient(@PathVariable Long ingredientId) {
        return alertService.getByIngredient(ingredientId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertDto> get(@PathVariable Long id) {
        AlertDto dto = alertService.getById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        alertService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        alertService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
