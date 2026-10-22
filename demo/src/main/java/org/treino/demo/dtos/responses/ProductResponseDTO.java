package org.treino.demo.dtos.responses;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductResponseDTO(
    UUID id,
    String name,
    String description,
    BigDecimal price,
    Integer quantityInStock,
    Integer minimumStock,
    boolean active,
    UUID categoryId,
    Instant createdAt,
    Instant updatedAt
) {
}
