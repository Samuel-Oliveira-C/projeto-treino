package org.treino.demo.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.treino.demo.dtos.requests.CustomerRequestDTO;
import org.treino.demo.dtos.responses.CustomerResponseDTO;
import org.treino.demo.entities.CustomerEntity;
import org.treino.demo.mappers.CustomerMapper;
import org.treino.demo.repositories.CustomerRepository;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    
    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Transactional
    public CustomerResponseDTO createCustomer(CustomerRequestDTO request) {
        CustomerEntity entity = customerMapper.toEntity(request);
        CustomerEntity savedEntity = customerRepository.save(entity);
        return customerMapper.toResponse(savedEntity);
    }

    @Transactional
    public CustomerResponseDTO updateCustomer(UUID id, CustomerRequestDTO request) {
        CustomerEntity existingEntity = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customerMapper.updateEntityFromDto(request, existingEntity);
        CustomerEntity updatedEntity = customerRepository.save(existingEntity);
        return customerMapper.toResponse(updatedEntity);
    }

    @Transactional(readOnly = true)
    public CustomerResponseDTO getCustomerById(UUID id) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return customerMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public Page<CustomerResponseDTO> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(customerMapper::toResponse);
    }

    @Transactional //REFATORAR QUANDO FIZER AS ASSOCIAÇÕES
    public void deleteCustomer(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found");
        }
        customerRepository.deleteById(id);
    }
}
