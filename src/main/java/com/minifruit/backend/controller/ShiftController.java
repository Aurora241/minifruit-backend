package com.minifruit.backend.controller;

import com.minifruit.backend.dto.ShiftRequest;
import com.minifruit.backend.dto.ShiftResponse;
import com.minifruit.backend.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping
    public ResponseEntity<?> closeShift(@RequestBody ShiftRequest req) {
        try {
            return ResponseEntity.ok(shiftService.closeShift(req));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    @GetMapping("/branch/{branchId}")
    public List<ShiftResponse> getByBranch(@PathVariable Long branchId) {
        return shiftService.getByBranch(branchId);
    }
}
