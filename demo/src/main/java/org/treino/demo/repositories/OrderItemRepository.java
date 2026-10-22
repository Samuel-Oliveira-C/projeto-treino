package org.treino.demo.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.treino.demo.entities.OrderItemEntity;
import org.treino.demo.entities.OrderItemId;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, OrderItemId> {
    Page<OrderItemEntity> findAllByIdOrderId(UUID orderId, Pageable pageable);
}
