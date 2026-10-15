package com.example.user_management_system.repository;

import com.example.user_management_system.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByStatus(Product.Status status);
    List<Product> findByCategoryIdAndStatus(Long categoryId, Product.Status status);
    Optional<Product> findByIdAndStatus(Long id, Product.Status status);
}
