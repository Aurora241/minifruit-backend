package com.minifruit.backend.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ShiftRequest {
    private Long userId;
    private Long branchId;
    private String startTime;
    private BigDecimal openingCash;
    private BigDecimal cashRevenue;
    private BigDecimal transferRevenue;
    private BigDecimal totalRevenue;
    private BigDecimal expectedCash;
    private BigDecimal actualCash;
    private BigDecimal difference;
    private String notes;
}
