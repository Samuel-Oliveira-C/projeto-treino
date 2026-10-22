package org.treino.demo.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.treino.demo.entities.OrderEntity;

import jakarta.persistence.LockModeType;

@Repository 
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from OrderEntity o where o.id = :orderId")
    Optional<OrderEntity> findByIdForUpdate(@Param("orderId") UUID orderId);
}
