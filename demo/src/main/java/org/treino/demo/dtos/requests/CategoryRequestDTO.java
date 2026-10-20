package org.treino.demo.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDTO(
    @NotBlank String name, 
    @NotBlank String description) {
    
}
