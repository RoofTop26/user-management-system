package com.example.user_management_system.dto.response;

import com.example.user_management_system.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserProfileResponse {

    private Long id;
    private String username;
    private String name;
    private String email;
    private LocalDate dob;
    private User.Status status;
    private LocalDateTime createdAt;

    public UserProfileResponse(Long id, String username, String name, String email, LocalDate dob, User.Status status, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public LocalDate getDob() { return dob; }
    public User.Status getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
