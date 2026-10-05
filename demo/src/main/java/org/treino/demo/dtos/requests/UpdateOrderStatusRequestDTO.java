package org.treino.demo.dtos.requests;

import org.treino.demo.enums.StatusOrderEntity;

import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequestDTO(
        @NotNull StatusOrderEntity status) {
}