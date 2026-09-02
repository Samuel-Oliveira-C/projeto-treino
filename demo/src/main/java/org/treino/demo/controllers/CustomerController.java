package org.treino.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.treino.demo.dtos.requests.CustomerRequestDTO;
import org.treino.demo.dtos.responses.CustomerResponseDTO;
import org.treino.demo.entities.CustomerEntity;
import org.treino.demo.services.CustomerService;

import jakarta.validation.Valid;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping()
    public ResponseEntity<CustomerResponseDTO> create(@Valid @RequestBody CustomerRequestDTO entity) {
        CustomerResponseDTO createdCustomer = customerService.createCustomer(entity);
        URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdCustomer.id())
                    .toUri();
        return ResponseEntity.created(uri).body(createdCustomer);
    }
}
