package org.treino.demo.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.treino.demo.entities.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    @Modifying
    @Query("""
            update ProductEntity product
            set product.quantityInStock = product.quantityInStock - :quantity,
                product.updatedAt = CURRENT_TIMESTAMP
            where product.id = :productId
              and product.active = true
              and product.quantityInStock >= :quantity
            """)
    int decreaseStockIfAvailable(
            @Param("productId") UUID productId,
            @Param("quantity") int quantity);
}
