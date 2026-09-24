package org.treino.demo.entities;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.treino.demo.enums.StatusOrderEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "orders_table")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Positive
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrderEntity status = StatusOrderEntity.WAITING_PAYMENT;

    @NotNull
    @ManyToOne(fetch =  FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @NotNull
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public OrderEntity() {}

    public OrderEntity(UUID id, BigDecimal totalAmount, StatusOrderEntity status, CustomerEntity customer) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.status = status;
        setCustomer(customer);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public StatusOrderEntity getStatus() {
        return status;
    }

    public void setStatus(StatusOrderEntity status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        if (this.customer == customer) {
            if (customer != null) {
                customer.addOrderReference(this);
            }
            return;
        }

        CustomerEntity previousCustomer = this.customer;
        this.customer = customer;

        if (previousCustomer != null) {
            previousCustomer.removeOrderReference(this);
        }

        if (customer != null) {
            customer.addOrderReference(this);
        }
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
        OrderEntity other = (OrderEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "OrderEntity [id=" + id + ", totalAmount=" + totalAmount + ", status=" + status + ", createdAt="
                + createdAt + "]";
    }
}
