package com.minifruit.backend.controller;

import com.minifruit.backend.entity.Order;
import com.minifruit.backend.entity.OrderDetail;
import com.minifruit.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    @GetMapping("/branch/{branchId}")
    public List<Order> getByBranch(@PathVariable Long branchId) {
        return orderService.getByBranch(branchId);
    }

    @GetMapping("/user/{userId}")
    public List<Order> getByUser(@PathVariable Long userId) {
        return orderService.getByUser(userId);
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable Long id) {
        return orderService.getById(id);
    }

    @GetMapping("/{id}/details")
    public List<OrderDetail> getDetails(@PathVariable Long id) {
        return orderService.getDetails(id);
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> body) {
        try {
            Object branchIdVal = body.get("branchId");
            if (branchIdVal == null) throw new IllegalArgumentException("branchId is required");
            Object userIdVal = body.get("userId");
            if (userIdVal == null) throw new IllegalArgumentException("userId is required");
            Long branchId = Long.valueOf(branchIdVal.toString());
            Long userId = Long.valueOf(userIdVal.toString());
            Long customerId = body.get("customerId") != null ?
                    Long.valueOf(body.get("customerId").toString()) : null;
            String paymentMethod = (String) body.getOrDefault("paymentMethod", "CASH");
            BigDecimal discount = body.get("discount") != null ?
                    new BigDecimal(body.get("discount").toString()) : BigDecimal.ZERO;
            List<Map<String, Object>> items =
                    (List<Map<String, Object>>) body.get("items");
            log.info("createOrder: branchId={}, userId={}, customerId={}, items={}",
                    branchId, userId, customerId, items == null ? 0 : items.size());
            return ResponseEntity.ok(orderService.createOrder(
                    branchId, userId, customerId, paymentMethod, discount, items));
        } catch (IllegalArgumentException e) {
            log.warn("createOrder bad request: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        } catch (RuntimeException e) {
            log.error("createOrder error", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }
}