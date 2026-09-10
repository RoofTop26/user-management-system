package com.example.user_management_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordChangeRequest {

    @NotBlank(message = "oldPassword không được để trống")
    private String oldPassword;

    @NotBlank(message = "newPassword không được để trống")
    @Size(min = 6, message = "newPassword phải có ít nhất 6 ký tự")
    private String newPassword;

    public String getOldPassword() { return oldPassword; }
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}