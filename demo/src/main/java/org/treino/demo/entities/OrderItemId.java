package org.treino.demo.entities;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class OrderItemId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    public OrderItemId() {}

    public OrderItemId(UUID orderId, UUID productId) {
        this.orderId = orderId;
        this.productId = productId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    @Override
    public int hashCode() {
        int result = orderId == null ? 0 : orderId.hashCode();
        return 31 * result + (productId == null ? 0 : productId.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        OrderItemId other = (OrderItemId) obj;
        return java.util.Objects.equals(orderId, other.orderId)
                && java.util.Objects.equals(productId, other.productId);
    }
}
