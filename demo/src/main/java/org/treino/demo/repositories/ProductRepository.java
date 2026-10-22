package org.treino.demo.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.treino.demo.entities.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
}
