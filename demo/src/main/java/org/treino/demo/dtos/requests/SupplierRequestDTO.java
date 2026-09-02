package org.treino.demo.dtos.requests;

import org.hibernate.validator.constraints.br.CNPJ;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SupplierRequestDTO(
    @NotBlank String name,
    @NotBlank @CNPJ String documentCNPJ,
    @NotBlank @Email String email,
    @NotBlank String phone
) {
    
}
