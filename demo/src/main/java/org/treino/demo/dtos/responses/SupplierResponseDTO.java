package org.treino.demo.dtos.responses;

import java.time.Instant;
import java.util.UUID;

public record SupplierResponseDTO(
    UUID id,
    String name,
    String documentCNPJ,
    String email,
    String phone,
    Instant createdAt
) {
    
}
