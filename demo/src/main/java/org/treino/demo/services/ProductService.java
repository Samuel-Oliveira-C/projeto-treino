package org.treino.demo.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.treino.demo.dtos.requests.ProductPatchRequestDTO;
import org.treino.demo.dtos.requests.ProductRequestDTO;
import org.treino.demo.dtos.responses.ProductResponseDTO;
import org.treino.demo.entities.CategoryEntity;
import org.treino.demo.entities.ProductEntity;
import org.treino.demo.exceptions.ResourceNotFoundException;
import org.treino.demo.mappers.ProductMapper;
import org.treino.demo.repositories.CategoryRepository;
import org.treino.demo.repositories.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        CategoryEntity category = findCategoryById(request.categoryId());
        ProductEntity product = productMapper.toEntity(request);
        product.setCategory(category);

        return productMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponseDTO updateProduct(UUID id, ProductRequestDTO request) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
        CategoryEntity category = findCategoryById(request.categoryId());

        productMapper.updateEntityFromDto(request, product);
        product.setCategory(category);

        return productMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponseDTO partialUpdateProduct(UUID id, ProductPatchRequestDTO request) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto não encontrado com ID: " + id));

        productMapper.updateEntityFromPatch(request, product);

        if (request.categoryId() != null) {
            product.setCategory(findCategoryById(request.categoryId()));
        }

    return productMapper.toResponse(productRepository.save(product));
}

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toResponse);
    }

    @Transactional (readOnly = true)
    public ProductResponseDTO getProductById(UUID id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
        return productMapper.toResponse(product);
    }

    @Transactional
    public void deleteProduct(UUID id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
        productRepository.delete(product);
    }

    private CategoryEntity findCategoryById(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria não encontrada com ID: " + categoryId));
    }
}
