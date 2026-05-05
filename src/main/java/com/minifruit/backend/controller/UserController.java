package com.minifruit.backend.controller;

import com.minifruit.backend.entity.User;
import com.minifruit.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping
    public User create(@RequestBody Map<String, Object> body) {
        User user = new User();
        user.setUsername((String) body.get("username"));
        user.setPasswordHash((String) body.get("password"));
        user.setFullName((String) body.get("fullName"));
        String roleName = (String) body.get("role");
        Long branchId = body.get("branchId") != null ?
                Long.valueOf(body.get("branchId").toString()) : null;
        return userService.create(user, roleName, branchId);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String roleName = (String) body.get("role");
        Long branchId = body.get("branchId") != null ?
                Long.valueOf(body.get("branchId").toString()) : null;
        String fullName = (String) body.get("fullName");
        String password = (String) body.get("password");
        return userService.update(id, fullName, roleName, branchId, password);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> toggleStatus(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        boolean status = Boolean.parseBoolean(body.get("status").toString());
        userService.setStatus(id, status);
        return ResponseEntity.ok(status ? "Đã kích hoạt tài khoản" : "Đã vô hiệu hóa tài khoản");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deactivate(@PathVariable Long id) {
        userService.deactivate(id);
        return ResponseEntity.ok("Đã vô hiệu hóa tài khoản");
    }
}