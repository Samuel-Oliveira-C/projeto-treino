package org.treino.demo.dtos.responses;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;


import org.treino.demo.enums.StatusOrderEntity;

public record OrderResponseDTO(
    UUID orderID, 
    BigDecimal totalAmount, 
    StatusOrderEntity status,
    UUID customerID,
    Instant createdAt) {
    
}
