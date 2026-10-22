package org.treino.demo.dtos.requests;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequestDTO(
    @NotBlank String name,
    @NotBlank String description,
    @NotNull @Positive BigDecimal price,
    @NotNull @Min(0) Integer quantityInStock,
    @NotNull @Min(0) Integer minimumStock,
    @NotNull Boolean active,
    @NotNull UUID categoryId
) {
}
