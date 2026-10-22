package org.treino.demo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.treino.demo.dtos.responses.OrderItemResponseDTO;
import org.treino.demo.entities.OrderItemEntity;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(source = "product.id", target = "productID")
    @Mapping(
        target = "subtotal",
        expression = "java(item.getUnitPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())))"
    )
    OrderItemResponseDTO toResponse(OrderItemEntity item);
}
