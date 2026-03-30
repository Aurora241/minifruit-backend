package com.minifruit.backend.repository;

import com.minifruit.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByBranchBranchId(Long branchId);
    List<Order> findByUserUserId(Long userId);
}