package org.treino.demo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.treino.demo.dtos.responses.OrderResponseDTO;
import org.treino.demo.entities.OrderEntity;

@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(source = "id", target = "orderID")
    @Mapping(source = "customer.id", target = "customerID")
    OrderResponseDTO toResponse(OrderEntity entity);
}