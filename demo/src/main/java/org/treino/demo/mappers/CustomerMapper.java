package org.treino.demo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.treino.demo.dtos.requests.CustomerRequestDTO;
import org.treino.demo.dtos.responses.CustomerResponseDTO;
import org.treino.demo.entities.CustomerEntity;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    
    CustomerEntity toEntity(CustomerRequestDTO dto);

    CustomerResponseDTO toResponse(CustomerEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(CustomerRequestDTO dto, @MappingTarget CustomerEntity entity);
}
