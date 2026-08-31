package org.treino.demo.services;

import org.springframework.stereotype.Service;
import org.treino.demo.dtos.requests.CustomerRequestDTO;
import org.treino.demo.dtos.responses.CustomerResponseDTO;
import org.treino.demo.entities.CustomerEntity;
import org.treino.demo.mappers.CustomerMapper;
import org.treino.demo.repositories.CustomerRepository;

import jakarta.transaction.Transactional;

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
}
