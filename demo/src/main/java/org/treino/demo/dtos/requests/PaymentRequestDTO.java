package org.treino.demo.dtos.requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentRequestDTO(
    @NotNull @Positive BigDecimal amountReceived
) {
}
