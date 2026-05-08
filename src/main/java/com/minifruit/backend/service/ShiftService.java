package com.minifruit.backend.service;

import com.minifruit.backend.dto.ShiftRequest;
import com.minifruit.backend.dto.ShiftResponse;
import com.minifruit.backend.entity.Shift;
import com.minifruit.backend.repository.BranchRepository;
import com.minifruit.backend.repository.ShiftRepository;
import com.minifruit.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;

    public ShiftResponse closeShift(ShiftRequest req) {
        Shift shift = new Shift();
        shift.setUser(userRepository.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng")));
        shift.setBranch(branchRepository.findById(req.getBranchId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi nhánh")));

        if (req.getStartTime() != null) {
            shift.setStartTime(LocalDateTime.parse(req.getStartTime()));
        }
        shift.setEndTime(LocalDateTime.now());

        shift.setOpeningCash(req.getOpeningCash());
        shift.setCashRevenue(req.getCashRevenue());
        shift.setTransferRevenue(req.getTransferRevenue());
        shift.setTotalRevenue(req.getTotalRevenue());
        shift.setExpectedCash(req.getExpectedCash());
        shift.setActualCash(req.getActualCash());
        shift.setDifference(req.getDifference());
        shift.setNotes(req.getNotes());

        return ShiftResponse.from(shiftRepository.save(shift));
    }

    public List<ShiftResponse> getByBranch(Long branchId) {
        return shiftRepository.findByBranchBranchIdOrderByCreatedAtDesc(branchId)
                .stream()
                .map(ShiftResponse::from)
                .collect(Collectors.toList());
    }
}
