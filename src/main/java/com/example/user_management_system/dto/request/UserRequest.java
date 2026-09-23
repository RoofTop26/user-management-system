package com.example.user_management_system.dto.request;

import com.example.user_management_system.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public class UserRequest {

    private String username;

    private String password;

    @NotBlank(message = "name không được để trống")
    private String name;

    private String email;

    @NotNull(message = "dob không được để trống")
    @Past(message = "dob phải là một ngày trong quá khứ")
    private LocalDate dob;

    private User.Status status;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public User.Status getStatus() { return status; }
    public void setStatus(User.Status status) { this.status = status; }
}
