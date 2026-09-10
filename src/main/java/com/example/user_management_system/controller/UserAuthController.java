package com.example.user_management_system.controller;

import com.example.user_management_system.dto.request.AdminRequest;
import com.example.user_management_system.dto.request.RegisterRequest;
import com.example.user_management_system.dto.request.UserRequest;
import com.example.user_management_system.dto.response.LoginResponse;
import com.example.user_management_system.dto.response.UserProfileResponse;
import com.example.user_management_system.entity.User;
import com.example.user_management_system.service.UserAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/portal")
public class UserAuthController {

    private final UserAuthService userAuthService;

    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        User created = userAuthService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AdminRequest request) {
        LoginResponse response = userAuthService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
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
}