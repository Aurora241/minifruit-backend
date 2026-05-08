package com.minifruit.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "shifts")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shiftId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private BigDecimal openingCash = BigDecimal.ZERO;
    private BigDecimal cashRevenue = BigDecimal.ZERO;
    private BigDecimal transferRevenue = BigDecimal.ZERO;
    private BigDecimal totalRevenue = BigDecimal.ZERO;
    private BigDecimal expectedCash = BigDecimal.ZERO;
    private BigDecimal actualCash = BigDecimal.ZERO;
    private BigDecimal difference = BigDecimal.ZERO;

    private String notes;
    private LocalDateTime createdAt = LocalDateTime.now();
}
