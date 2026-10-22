package org.treino.demo.dtos.responses;

import java.util.UUID;

public record CategoryResponseDTO(
    UUID id,
    String name,
    String description) {
    
}
