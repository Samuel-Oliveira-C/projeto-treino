package org.treino.demo.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.treino.demo.dtos.requests.CategoryRequestDTO;
import org.treino.demo.dtos.responses.CategoryResponseDTO;
import org.treino.demo.entities.CategoryEntity;
import org.treino.demo.exceptions.ResourceNotFoundException;
import org.treino.demo.mappers.CategoryMapper;
import org.treino.demo.repositories.CategoryRepository;

@Service 
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO request) {
        CategoryEntity entity = categoryMapper.toEntity(request);
        CategoryEntity savedEntity = categoryRepository.save(entity);
        return categoryMapper.toResponse(savedEntity);
    }

    @Transactional
    public CategoryResponseDTO updateCategory(UUID id, CategoryRequestDTO request) {
        CategoryEntity existingEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        categoryMapper.updateEntityFromDto(request, existingEntity);
        CategoryEntity updatedEntity = categoryRepository.save(existingEntity);
        return categoryMapper.toResponse(updatedEntity);
    }

    @Transactional(readOnly = true)
    public CategoryResponseDTO getCategoryById(UUID id) {
        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return categoryMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponseDTO> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toResponse);
    }

    @Transactional
    public void deleteCategory(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found");
        }
        categoryRepository.deleteById(id);
    }
}