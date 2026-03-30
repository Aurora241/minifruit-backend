package com.minifruit.backend.repository;

import com.minifruit.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStatus(Boolean status);
    List<Product> findByCategoryCategoryId(Long categoryId);
}