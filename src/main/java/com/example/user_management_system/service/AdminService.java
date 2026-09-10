package com.example.user_management_system.service;

import com.example.user_management_system.dto.response.LoginResponse;
import com.example.user_management_system.entity.Admin;
import com.example.user_management_system.exception.InvalidLoginException;
import com.example.user_management_system.repository.AdminRepository;
import com.example.user_management_system.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Admin createAdmin(String username, String rawPassword) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(rawPassword));
        return adminRepository.save(admin);
    }

    public LoginResponse login(String username, String rawPassword) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(InvalidLoginException::new);

        if (!passwordEncoder.matches(rawPassword, admin.getPassword())) {
            throw new InvalidLoginException();
        }

        String token = jwtUtil.generateToken(admin.getUsername(), "ADMIN");
        return new LoginResponse(token, admin.getUsername(), "ADMIN");
    }
}