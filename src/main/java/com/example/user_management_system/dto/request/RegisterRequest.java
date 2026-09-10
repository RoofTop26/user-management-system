package com.example.user_management_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class RegisterRequest {

    @NotBlank(message = "username không được để trống")
    private String username;

    @Size(min = 6, message = "password phải có ít nhất 6 ký tự")
    private String password;

    @NotBlank(message = "name không được để trống")
    private String name;

    @NotNull(message = "ngày sinh không được để trống")
    @Past(message = "ngày sinh phải là một ngày trong quá khứ")
    private LocalDate dob;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
}