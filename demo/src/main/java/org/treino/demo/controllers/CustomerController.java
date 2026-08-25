package org.treino.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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
    public ResponseEntity<CustomerEntity> create(@Valid @RequestBody CustomerEntity entity) {
        CustomerEntity createdCustomer = customerService.createCustomer(entity);
        URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdCustomer.getId())
                    .toUri();
        return ResponseEntity.created(uri).body(createdCustomer);
    }
    
}
