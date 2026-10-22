package org.treino.demo.dtos.responses;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDTO(
    UUID productID,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal subtotal
) {
}
