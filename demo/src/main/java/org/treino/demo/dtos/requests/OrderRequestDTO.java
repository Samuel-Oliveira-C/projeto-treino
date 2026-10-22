package org.treino.demo.dtos.requests;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.Valid;

public record OrderRequestDTO(
    @NotNull UUID customerID,
    @NotEmpty List<@Valid OrderItemRequestDTO> items
) {
}
