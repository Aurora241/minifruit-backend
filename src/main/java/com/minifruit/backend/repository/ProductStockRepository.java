package com.minifruit.backend.repository;

import com.minifruit.backend.entity.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {
    List<ProductStock> findByBranchBranchId(Long branchId);
    Optional<ProductStock> findByProductProductIdAndBranchBranchId(Long productId, Long branchId);
    List<ProductStock> findByCurrentStockLessThanEqualAndBranchBranchId(Integer threshold, Long branchId);
}