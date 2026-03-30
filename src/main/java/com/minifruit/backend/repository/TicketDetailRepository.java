package com.minifruit.backend.repository;

import com.minifruit.backend.entity.TicketDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketDetailRepository extends JpaRepository<TicketDetail, Long> {
    List<TicketDetail> findByTicketTicketId(Long ticketId);
}