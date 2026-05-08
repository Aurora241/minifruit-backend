package com.minifruit.backend.repository;

import com.minifruit.backend.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findByBranchBranchIdOrderByCreatedAtDesc(Long branchId);
}
