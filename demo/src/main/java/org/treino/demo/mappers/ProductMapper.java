package org.treino.demo.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.treino.demo.dtos.requests.ProductPatchRequestDTO;
import org.treino.demo.dtos.requests.ProductRequestDTO;
import org.treino.demo.dtos.responses.ProductResponseDTO;
import org.treino.demo.entities.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    ProductEntity toEntity(ProductRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEntityFromDto(ProductRequestDTO dto, @MappingTarget ProductEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEntityFromPatch(ProductPatchRequestDTO dto,@MappingTarget ProductEntity entity);

    @Mapping(source = "category.id", target = "categoryId")
    ProductResponseDTO toResponse(ProductEntity entity);
}
