package com.pharmacy.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AlertDto {
    private Long id;
    private Long ingredientId;
    private String ingredientName;
    private String alertType;
    private String message;
    private LocalDateTime timestamp;
    private Boolean isRead;
}
