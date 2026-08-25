package org.treino.demo.services;

import org.springframework.stereotype.Service;
import org.treino.demo.entities.CustomerEntity;
import org.treino.demo.repositories.CustomerRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerEntity createCustomer(CustomerEntity customer) {
        CustomerEntity savedCustomer = new CustomerEntity();
        savedCustomer.setName(customer.getName());
        savedCustomer.setEmail(customer.getEmail());
        savedCustomer.setPhone(customer.getPhone());
        return customerRepository.save(savedCustomer);
    }
}
