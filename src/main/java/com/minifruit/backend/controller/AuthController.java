package com.minifruit.backend.controller;

import com.minifruit.backend.dto.LoginRequest;
import com.minifruit.backend.dto.LoginResponse;
import com.minifruit.backend.repository.UserRepository;
import com.minifruit.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Sai tên đăng nhập hoặc mật khẩu");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi: " + e.getMessage());
        }

        var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().getRoleName());

        return ResponseEntity.ok(new LoginResponse(
                token,
                user.getUsername(),
                user.getRole().getRoleName(),
                user.getBranch() != null ? user.getBranch().getBranchId() : null
        ));
    }
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/test-hash")
    public ResponseEntity<?> testHash() {
        String hash = passwordEncoder.encode("password");
        boolean matches = passwordEncoder.matches("password", "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.");
        return ResponseEntity.ok("Hash mới: " + hash + " | Hash cũ khớp không: " + matches);
    }
}