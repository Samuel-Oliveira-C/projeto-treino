package org.treino.demo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.treino.demo.dtos.requests.OrderRequestDTO;
import org.treino.demo.dtos.responses.OrderResponseDTO;
import org.treino.demo.entities.OrderEntity;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "customer", ignore = true)
    OrderEntity toEntity(OrderRequestDTO dto);

    @Mapping(source = "id", target = "orderID")
    @Mapping(source = "customer.id", target = "customerID")
    OrderResponseDTO toResponse(OrderEntity entity);
}