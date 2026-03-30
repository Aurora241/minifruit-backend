package com.minifruit.backend.controller;

import com.minifruit.backend.entity.Branch;
import com.minifruit.backend.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @GetMapping
    public List<Branch> getAll() {
        return branchService.getAll();
    }

    @GetMapping("/{id}")
    public Branch getById(@PathVariable Long id) {
        return branchService.getById(id);
    }

    @PostMapping
    public Branch create(@RequestBody Branch branch) {
        return branchService.create(branch);
    }

    @PutMapping("/{id}")
    public Branch update(@PathVariable Long id, @RequestBody Branch branch) {
        return branchService.update(id, branch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        branchService.delete(id);
        return ResponseEntity.ok("Đã vô hiệu hóa chi nhánh");
    }
}