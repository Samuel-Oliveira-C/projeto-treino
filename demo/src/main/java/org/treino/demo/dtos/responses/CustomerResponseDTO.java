package org.treino.demo.dtos.responses;

import java.time.Instant;
import java.util.UUID;

public record CustomerResponseDTO(
    UUID id,
    String name,
    String email,
    String phone,
    Instant createdAt
) {}