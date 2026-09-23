package org.treino.demo.dtos.requests;

import java.math.BigDecimal;
import java.util.UUID;

import org.treino.demo.enums.StatusOrderEntity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderRequestDTO(
    UUID orderID,

    @NotNull @Positive BigDecimal totalAmount, 
    
    StatusOrderEntity status, 
    
    @NotNull UUID customerID) {
    
    public OrderRequestDTO{
        if(status == null){
            status = StatusOrderEntity.WAITING_PAYMENT;
        }
    }
}
