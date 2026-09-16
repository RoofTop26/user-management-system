package com.example.user_management_system.service;

import com.example.user_management_system.dto.request.AdminRequest;
import com.example.user_management_system.dto.response.LoginResponse;
import com.example.user_management_system.entity.Admin;
import com.example.user_management_system.exception.AccessDeniedException;
import com.example.user_management_system.exception.InvalidLoginException;
import com.example.user_management_system.repository.AdminRepository;
import com.example.user_management_system.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin getAdminById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Admin với id: " + id));
    }

    public Admin updateAdmin(Long id, AdminRequest request) {
        Admin admin = getAdminById(id);
        admin.setUsername(request.getUsername());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        return adminRepository.save(admin);
    }

    public Admin patchAdmin(Long id, AdminRequest request) {
        Admin admin = getAdminById(id);
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            admin.setUsername(request.getUsername());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return adminRepository.save(admin);
    }

    public void deleteAdmin(Long id, String callerUsername) {
        Admin admin = getAdminById(id);
        if (admin.getUsername().equals(callerUsername)) {
            throw new AccessDeniedException("Không thể tự xóa chính mình");
        }
        adminRepository.delete(admin);
    }
}