package org.treino.demo.entities;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "customers_table")
public class CustomerEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;
    
    @NotBlank
    @Column(nullable = false)
    private String phone;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @OneToMany(
        mappedBy = "customer",
        orphanRemoval = true,
        cascade = CascadeType.ALL
    )
    private List<OrderEntity> orders = new ArrayList<>();
    
    public CustomerEntity() {
    }

    public CustomerEntity(UUID id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
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
    public void prePersist(){
        createdAt = Instant.now();
    }

    public List<OrderEntity> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public void addOrder(OrderEntity order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        order.setCustomer(this);
    }

    public void removeOrder(OrderEntity order) {
        if (order == null) {
            throw new IllegalArgumentException("Order not found in the customer's orders");
        }

        if (order.getCustomer() == this) {
            order.setCustomer(null);
        }
    }

    void addOrderReference(OrderEntity order) {
        if (!orders.contains(order)) {
            orders.add(order);
        }
    }

    void removeOrderReference(OrderEntity order) {
        orders.remove(order);
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
        CustomerEntity other = (CustomerEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "CustomerEntity [id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone + ", createdAt="
                + createdAt + "]";
    }
}