package com.example.user_management_system.controller;

import com.example.user_management_system.dto.request.AdminRequest;
import com.example.user_management_system.dto.response.LoginResponse;
import com.example.user_management_system.entity.Admin;
import com.example.user_management_system.service.AdminService;
import com.example.user_management_system.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    public ResponseEntity<Admin> createAdmin(@RequestBody AdminRequest request) {
        Admin created = adminService.createAdmin(request.getUsername(), request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AdminRequest request) {
        Admin admin = adminService.login(request.getUsername(), request.getPassword());

        String token = JwtUtil.generateToken(admin.getUsername());
        return ResponseEntity.ok(new LoginResponse(token, admin.getUsername()));
    }
}