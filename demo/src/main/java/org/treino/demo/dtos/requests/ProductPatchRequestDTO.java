package org.treino.demo.dtos.requests;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public record ProductPatchRequestDTO(
    String name,
    String description,
    @Positive BigDecimal price,
    @Min(0) Integer quantityInStock,
    @Min(0) Integer minimumStock,
    Boolean active,
    UUID categoryId
) {}