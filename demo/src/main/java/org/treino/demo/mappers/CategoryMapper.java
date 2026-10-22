package org.treino.demo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.treino.demo.dtos.requests.CategoryRequestDTO;
import org.treino.demo.dtos.responses.CategoryResponseDTO;
import org.treino.demo.entities.CategoryEntity;

@Mapper (componentModel = "spring")
public interface CategoryMapper {
    CategoryEntity toEntity(CategoryRequestDTO dto);

    CategoryResponseDTO toResponse(CategoryEntity entity);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(CategoryRequestDTO dto, @MappingTarget CategoryEntity entity);

    @Mapping(target = "id", ignore = true)
    CategoryResponseDTO toResponseWithoutId(CategoryEntity entity);

}
