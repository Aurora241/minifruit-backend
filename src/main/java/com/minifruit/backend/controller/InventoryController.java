package com.minifruit.backend.controller;

import com.minifruit.backend.entity.InventoryTicket;
import com.minifruit.backend.entity.ProductStock;
import com.minifruit.backend.repository.UserRepository;
import com.minifruit.backend.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private static final Logger log = LoggerFactory.getLogger(InventoryController.class);

    private final InventoryService inventoryService;
    private final UserRepository userRepository;

    @GetMapping("/tickets")
    public List<InventoryTicket> getTickets(
            @RequestParam(required = false) Long branchId) {
        return inventoryService.getTickets(branchId);
    }

    @GetMapping("/tickets/{id}")
    public Map<String, Object> getTicketDetail(@PathVariable Long id) {
        return inventoryService.getTicketDetail(id);
    }

    @GetMapping("/stock/{branchId}")
    public List<ProductStock> getStock(@PathVariable Long branchId) {
        return inventoryService.getStockByBranch(branchId);
    }

    @GetMapping("/low-stock/{branchId}")
    public List<ProductStock> getLowStock(@PathVariable Long branchId) {
        return inventoryService.getLowStock(branchId);
    }

    /** Lấy userId từ body; nếu thiếu thì fallback từ JWT (SecurityContext). */
    private Long resolveUserId(Map<String, Object> body) {
        Object val = body.get("userId");
        if (val != null) {
            try { return Long.valueOf(val.toString()); } catch (NumberFormatException ignored) {}
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .map(u -> u.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user hiện tại"));
    }

    private static Map<String, Object> err(String message) {
        return Map.of("success", false, "message", message);
    }

    @PostMapping("/import")
    public ResponseEntity<?> importStock(@RequestBody Map<String, Object> body) {
        try {
            Object branchIdVal = body.get("branchId");
            if (branchIdVal == null) throw new IllegalArgumentException("branchId is required");
            Long branchId = Long.valueOf(branchIdVal.toString());
            Long userId = resolveUserId(body);
            String note = (String) body.getOrDefault("note", "");
            List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");
            log.info("importStock: branchId={}, userId={}, items={}", branchId, userId,
                    items == null ? 0 : items.size());
            return ResponseEntity.ok(inventoryService.importStock(branchId, userId, note, items));
        } catch (IllegalArgumentException e) {
            log.warn("importStock bad request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(err(e.getMessage()));
        } catch (RuntimeException e) {
            log.error("importStock error", e);
            return ResponseEntity.badRequest().body(err(e.getMessage()));
        }
    }

    @PostMapping("/export")
    public ResponseEntity<?> exportStock(@RequestBody Map<String, Object> body) {
        try {
            Object branchIdVal = body.get("branchId");
            if (branchIdVal == null) throw new IllegalArgumentException("branchId is required");
            Long branchId = Long.valueOf(branchIdVal.toString());
            Long userId = resolveUserId(body);
            String note = (String) body.getOrDefault("note", "");
            List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");
            log.info("exportStock: branchId={}, userId={}, items={}", branchId, userId,
                    items == null ? 0 : items.size());
            return ResponseEntity.ok(inventoryService.exportStock(branchId, userId, note, items));
        } catch (IllegalArgumentException e) {
            log.warn("exportStock bad request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(err(e.getMessage()));
        } catch (RuntimeException e) {
            log.error("exportStock error", e);
            return ResponseEntity.badRequest().body(err(e.getMessage()));
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferStock(@RequestBody Map<String, Object> body) {
        try {
            Object fromBranchIdVal = body.get("fromBranchId");
            if (fromBranchIdVal == null) throw new IllegalArgumentException("fromBranchId is required");
            Object toBranchIdVal = body.get("toBranchId");
            if (toBranchIdVal == null) throw new IllegalArgumentException("toBranchId is required");
            Long fromBranchId = Long.valueOf(fromBranchIdVal.toString());
            Long toBranchId = Long.valueOf(toBranchIdVal.toString());
            Long userId = resolveUserId(body);
            String note = (String) body.getOrDefault("note", "");
            List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");
            log.info("transferStock: fromBranchId={}, toBranchId={}, userId={}, items={}",
                    fromBranchId, toBranchId, userId, items == null ? 0 : items.size());
            return ResponseEntity.ok(inventoryService.transferStock(
                    fromBranchId, toBranchId, userId, note, items));
        } catch (IllegalArgumentException e) {
            log.warn("transferStock bad request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(err(e.getMessage()));
        } catch (RuntimeException e) {
            log.error("transferStock error", e);
            return ResponseEntity.badRequest().body(err(e.getMessage()));
        }
    }
}