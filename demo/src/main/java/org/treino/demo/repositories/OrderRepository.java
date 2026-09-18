package org.treino.demo.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.treino.demo.entities.OrderEntity;

@Repository 
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    
}
