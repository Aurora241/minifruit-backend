package com.minifruit.backend.repository;

import com.minifruit.backend.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findByStatus(Boolean status);
}