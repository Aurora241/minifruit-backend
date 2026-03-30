package com.minifruit.backend.repository;

import com.minifruit.backend.entity.InventoryTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InventoryTicketRepository extends JpaRepository<InventoryTicket, Long> {
    List<InventoryTicket> findByBranchBranchId(Long branchId);
    List<InventoryTicket> findByTicketType(String ticketType);
}