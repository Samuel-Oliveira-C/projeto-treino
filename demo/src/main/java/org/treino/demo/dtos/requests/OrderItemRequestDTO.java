package org.treino.demo.dtos.requests;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemRequestDTO(
    @NotNull UUID productID,
    @NotNull @Positive Integer quantity
) {
}
