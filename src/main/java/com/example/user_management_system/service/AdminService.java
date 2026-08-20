package com.example.user_management_system.service;

import com.example.user_management_system.entity.Admin;
import com.example.user_management_system.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.user_management_system.exception.InvalidLoginException;

import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Admin createAdmin(String username, String rawPassword) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(rawPassword));
        return adminRepository.save(admin);
    }

    public Admin login(String username, String rawPassword) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(InvalidLoginException::new);

        if (!passwordEncoder.matches(rawPassword, admin.getPassword())) {
            throw new InvalidLoginException();
        }

        return admin;
    }
}