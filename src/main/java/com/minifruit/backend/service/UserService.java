package com.minifruit.backend.service;

import com.minifruit.backend.entity.User;
import com.minifruit.backend.repository.BranchRepository;
import com.minifruit.backend.repository.RoleRepository;
import com.minifruit.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
    }

    public User create(User user, String roleName, Long branchId) {
        var role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role"));
        user.setRole(role);
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        if (branchId != null) {
            user.setBranch(branchRepository.findById(branchId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy chi nhánh")));
        }
        return userRepository.save(user);
    }

    public User update(Long id, String fullName, String roleName, Long branchId, String password) {
        User user = getById(id);
        if (fullName != null) user.setFullName(fullName);
        if (roleName != null) {
            var role = roleRepository.findByRoleName(roleName)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy role"));
            user.setRole(role);
        }
        if (branchId != null) {
            user.setBranch(branchRepository.findById(branchId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy chi nhánh")));
        } else {
            user.setBranch(null);
        }
        if (password != null && !password.isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(password));
        }
        return userRepository.save(user);
    }

    public void setStatus(Long id, boolean status) {
        User user = getById(id);
        user.setStatus(status);
        userRepository.save(user);
    }

    public void deactivate(Long id) {
        User user = getById(id);
        user.setStatus(false);
        userRepository.save(user);
    }
}