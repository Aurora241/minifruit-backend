package com.minifruit.backend.dto;

import com.minifruit.backend.entity.Shift;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ShiftResponse {
    private Long shiftId;
    private Long userId;
    private String username;
    private String fullName;
    private Long branchId;
    private String branchName;
    private String startTime;
    private String endTime;
    private BigDecimal openingCash;
    private BigDecimal cashRevenue;
    private BigDecimal transferRevenue;
    private BigDecimal totalRevenue;
    private BigDecimal expectedCash;
    private BigDecimal actualCash;
    private BigDecimal difference;
    private String notes;
    private String createdAt;

    public static ShiftResponse from(Shift s) {
        ShiftResponse r = new ShiftResponse();
        r.shiftId = s.getShiftId();
        r.userId = s.getUser().getUserId();
        r.username = s.getUser().getUsername();
        r.fullName = s.getUser().getFullName();
        r.branchId = s.getBranch().getBranchId();
        r.branchName = s.getBranch().getBranchName();
        r.startTime = s.getStartTime() != null ? s.getStartTime().toString() : null;
        r.endTime = s.getEndTime() != null ? s.getEndTime().toString() : null;
        r.openingCash = s.getOpeningCash();
        r.cashRevenue = s.getCashRevenue();
        r.transferRevenue = s.getTransferRevenue();
        r.totalRevenue = s.getTotalRevenue();
        r.expectedCash = s.getExpectedCash();
        r.actualCash = s.getActualCash();
        r.difference = s.getDifference();
        r.notes = s.getNotes();
        r.createdAt = s.getCreatedAt() != null ? s.getCreatedAt().toString() : null;
        return r;
    }
}
