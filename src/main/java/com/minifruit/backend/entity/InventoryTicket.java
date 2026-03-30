package com.minifruit.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "inventory_tickets")
public class InventoryTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    private String ticketType; // IMPORT, EXPORT, TRANSFER, AUDIT

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "to_branch_id")
    private Branch toBranch;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;

    private String note;
    private LocalDateTime createdAt = LocalDateTime.now();
}