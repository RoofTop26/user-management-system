package com.example.user_management_system.repository;

import com.example.user_management_system.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    List<Category> findByStatus(Category.Status status);
    Optional<Category> findByIdAndStatus(Long id, Category.Status status);
}
