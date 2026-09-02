package org.treino.demo.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.treino.demo.entities.SupplierEntity;

public interface SupplierRepository extends JpaRepository<SupplierEntity, UUID> {
    
}
