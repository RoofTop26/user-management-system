package com.example.user_management_system.controller;

import com.example.user_management_system.dto.request.AdminRequest;
import com.example.user_management_system.dto.response.AdminResponse;
import com.example.user_management_system.dto.response.LoginResponse;
import com.example.user_management_system.entity.Admin;
import com.example.user_management_system.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/admins")
    public ResponseEntity<AdminResponse> createAdmin(@RequestBody AdminRequest request) {
        Admin created = adminService.createAdmin(request.getUsername(), request.getPassword());
        AdminResponse response = new AdminResponse(created.getId(), created.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AdminRequest request) {
        LoginResponse response = adminService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admins")
    public ResponseEntity<List<AdminResponse>> getAllAdmins() {
        List<AdminResponse> response = adminService.getAllAdmins().stream()
                .map(admin -> new AdminResponse(admin.getId(), admin.getUsername()))
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admins/{id}")
    public ResponseEntity<AdminResponse> getAdminById(@PathVariable Long id) {
        Admin admin = adminService.getAdminById(id);
        return ResponseEntity.ok(new AdminResponse(admin.getId(), admin.getUsername()));
    }

    @PutMapping("/admins/{id}")
    public ResponseEntity<AdminResponse> updateAdmin(@PathVariable Long id, @RequestBody AdminRequest request) {
        Admin admin = adminService.updateAdmin(id, request);
        return ResponseEntity.ok(new AdminResponse(admin.getId(), admin.getUsername()));
    }

    @PatchMapping("/admins/{id}")
    public ResponseEntity<AdminResponse> patchAdmin(@PathVariable Long id, @RequestBody AdminRequest request) {
        Admin admin = adminService.patchAdmin(id, request);
        return ResponseEntity.ok(new AdminResponse(admin.getId(), admin.getUsername()));
    }

    @DeleteMapping("/admins/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id, HttpServletRequest request) {
        String callerUsername = (String) request.getAttribute("authenticatedUser");
        adminService.deleteAdmin(id, callerUsername);
        return ResponseEntity.noContent().build();
    }
}