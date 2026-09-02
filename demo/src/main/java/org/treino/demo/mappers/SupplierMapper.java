package org.treino.demo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.treino.demo.dtos.requests.SupplierRequestDTO;
import org.treino.demo.dtos.responses.SupplierResponseDTO;
import org.treino.demo.entities.SupplierEntity;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    
    SupplierEntity toEntity(SupplierRequestDTO dto);

    SupplierResponseDTO toResponse(SupplierEntity entity);

    @Mapping(target = "documentCNPJ", ignore = true)
    SupplierResponseDTO toResponseWithoutDocumentCNPJ(SupplierEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(SupplierRequestDTO dto, @MappingTarget SupplierEntity entity);
}
