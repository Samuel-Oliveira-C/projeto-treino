package org.treino.demo.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity 
@Table (name = "categories")
public class CategoryEntity {
    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column (nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false, length = 2000)
    private String description;

    public CategoryEntity() {}

    public CategoryEntity(UUID id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        CategoryEntity other = (CategoryEntity) obj;
        return id != null && id.equals(other.id);
    }

    @Override
    public String toString() {
        return "CategoryEntity [id=" + id + ", name=" + name + ", description=" + description + "]";
    }
}
