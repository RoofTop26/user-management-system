package com.example.user_management_system.controller;

import com.example.user_management_system.dto.request.AdminRequest;
import com.example.user_management_system.dto.request.ForgotPasswordRequest;
import com.example.user_management_system.dto.request.RegisterRequest;
import com.example.user_management_system.dto.request.ResetPasswordRequest;
import com.example.user_management_system.dto.request.UserRequest;
import com.example.user_management_system.dto.response.LoginResponse;
import com.example.user_management_system.dto.response.UserProfileResponse;
import com.example.user_management_system.service.UserAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.user_management_system.dto.request.PasswordChangeRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/portal")
public class UserAuthController {

    private final UserAuthService userAuthService;

    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserProfileResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserProfileResponse created = userAuthService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AdminRequest request) {
        LoginResponse response = userAuthService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        userAuthService.forgotPassword(request.getEmail());
        Map<String, String> body = new HashMap<>();
        body.put("message", "Nếu email tồn tại trong hệ thống, chúng tôi đã gửi hướng dẫn đặt lại mật khẩu");
        return ResponseEntity.ok(body);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        userAuthService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile(HttpServletRequest request) {
        String username = (String) request.getAttribute("authenticatedUser");
        UserProfileResponse profile = userAuthService.getProfile(username);
        return ResponseEntity.ok(profile);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserProfileResponse> updateMyProfile(HttpServletRequest request,
            @RequestBody UserRequest updateRequest) {
        String username = (String) request.getAttribute("authenticatedUser");
        UserProfileResponse profile = userAuthService.updateProfile(username, updateRequest);
        return ResponseEntity.ok(profile);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(HttpServletRequest request,
            @Valid @RequestBody PasswordChangeRequest changeRequest) {
        String username = (String) request.getAttribute("authenticatedUser");
        userAuthService.changePassword(username, changeRequest.getOldPassword(), changeRequest.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
