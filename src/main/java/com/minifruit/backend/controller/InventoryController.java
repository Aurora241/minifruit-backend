package com.minifruit.backend.controller;

import com.minifruit.backend.entity.InventoryTicket;
import com.minifruit.backend.entity.ProductStock;
import com.minifruit.backend.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/tickets")
    public List<InventoryTicket> getTickets(
            @RequestParam(required = false) Long branchId) {
        return inventoryService.getTickets(branchId);
    }

    @GetMapping("/stock/{branchId}")
    public List<ProductStock> getStock(@PathVariable Long branchId) {
        return inventoryService.getStockByBranch(branchId);
    }

    @GetMapping("/low-stock/{branchId}")
    public List<ProductStock> getLowStock(@PathVariable Long branchId) {
        return inventoryService.getLowStock(branchId);
    }

    @PostMapping("/import")
    public ResponseEntity<?> importStock(@RequestBody Map<String, Object> body) {
        Long branchId = Long.valueOf(body.get("branchId").toString());
        Long userId = Long.valueOf(body.get("userId").toString());
        String note = (String) body.getOrDefault("note", "");
        List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");
        return ResponseEntity.ok(inventoryService.importStock(branchId, userId, note, items));
    }

    @PostMapping("/export")
    public ResponseEntity<?> exportStock(@RequestBody Map<String, Object> body) {
        Long branchId = Long.valueOf(body.get("branchId").toString());
        Long userId = Long.valueOf(body.get("userId").toString());
        String note = (String) body.getOrDefault("note", "");
        List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");
        return ResponseEntity.ok(inventoryService.exportStock(branchId, userId, note, items));
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferStock(@RequestBody Map<String, Object> body) {
        Long fromBranchId = Long.valueOf(body.get("fromBranchId").toString());
        Long toBranchId = Long.valueOf(body.get("toBranchId").toString());
        Long userId = Long.valueOf(body.get("userId").toString());
        String note = (String) body.getOrDefault("note", "");
        List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");
        return ResponseEntity.ok(inventoryService.transferStock(
                fromBranchId, toBranchId, userId, note, items));
    }
}