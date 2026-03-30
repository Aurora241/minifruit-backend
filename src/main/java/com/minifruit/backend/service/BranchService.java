package com.minifruit.backend.service;

import com.minifruit.backend.entity.Branch;
import com.minifruit.backend.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;

    public List<Branch> getAll() {
        return branchRepository.findAll();
    }

    public Branch getById(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi nhánh"));
    }

    public Branch create(Branch branch) {
        return branchRepository.save(branch);
    }

    public Branch update(Long id, Branch data) {
        Branch branch = getById(id);
        branch.setBranchName(data.getBranchName());
        branch.setAddress(data.getAddress());
        branch.setStatus(data.getStatus());
        return branchRepository.save(branch);
    }

    public void delete(Long id) {
        Branch branch = getById(id);
        branch.setStatus(false);
        branchRepository.save(branch);
    }
}