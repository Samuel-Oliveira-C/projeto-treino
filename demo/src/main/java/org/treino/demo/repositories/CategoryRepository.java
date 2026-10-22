package org.treino.demo.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.treino.demo.entities.CategoryEntity;

@Repository 
public interface CategoryRepository extends JpaRepository<CategoryEntity,UUID> {
    
}
