package org.treino.demo.entities;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.validator.constraints.br.CNPJ;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "suppliers_table")
public class SupplierEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @CNPJ
    @Column(nullable = false, unique = true)
    private String documentCNPJ;

    @NotBlank
    @Column(nullable = false)
    @Email
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String phone;

    @NotNull
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public SupplierEntity() {}

    public SupplierEntity(UUID id, String name, String documentCNPJ, String email, String phone) {
        this.id = id;
        this.name = name;
        this.documentCNPJ = documentCNPJ;
        this.email = email;
        this.phone = phone;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentCNPJ() {
        return documentCNPJ;
    }

    public void setDocumentCNPJ(String documentCNPJ) {
        this.documentCNPJ = documentCNPJ;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SupplierEntity other = (SupplierEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SupplierEntity [id=" + id + ", name=" + name + ", documentCNPJ=" + documentCNPJ + ", email=" + email
                + ", phone=" + phone + ", createdAt=" + createdAt + "]";
    }
}
