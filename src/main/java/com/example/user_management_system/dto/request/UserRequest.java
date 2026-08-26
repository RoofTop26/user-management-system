package com.example.user_management_system.dto.request;

import com.example.user_management_system.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public class UserRequest {

    @NotBlank(message = "name không được để trống")
    private String name;

    @NotNull(message = "dob không được để trống")
    @Past(message = "dob phải là một ngày trong quá khứ")
    private LocalDate dob;

    private User.Status status;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public User.Status getStatus() { return status; }
    public void setStatus(User.Status status) { this.status = status; }
}