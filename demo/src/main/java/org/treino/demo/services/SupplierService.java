package org.treino.demo.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.treino.demo.dtos.requests.SupplierRequestDTO;
import org.treino.demo.dtos.responses.SupplierResponseDTO;
import org.treino.demo.entities.SupplierEntity;
import org.treino.demo.mappers.SupplierMapper;
import org.treino.demo.repositories.SupplierRepository;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public SupplierService(SupplierRepository supplierRepository, SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    @Transactional
    public SupplierResponseDTO createSupplier(SupplierRequestDTO request) {
        SupplierEntity entity = supplierMapper.toEntity(request);
        SupplierEntity savedEntity = supplierRepository.save(entity);
        return supplierMapper.toResponse(savedEntity);
    }

    @Transactional(readOnly = true)
    public SupplierResponseDTO getSupplierById(UUID id) {
        SupplierEntity entity = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        return supplierMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public Page<SupplierResponseDTO> getAllSuppliers(Pageable pageable) {
        return supplierRepository.findAll(pageable)
                .map(supplierMapper::toResponse);
    }

    @Transactional
    public SupplierResponseDTO updateSupplier(UUID id, SupplierRequestDTO request) {
        SupplierEntity existingEntity = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        supplierMapper.updateEntityFromDto(request, existingEntity);
        SupplierEntity updatedEntity = supplierRepository.save(existingEntity);
        return supplierMapper.toResponse(updatedEntity);
    }

    @Transactional //REFATORAR QUANDO FIZER AS ASSOCIAÇÕES
    public void deleteSupplier(UUID id) {
        if (!supplierRepository.existsById(id)) {
            throw new RuntimeException("Supplier not found");
        }
        supplierRepository.deleteById(id);
    }

}
