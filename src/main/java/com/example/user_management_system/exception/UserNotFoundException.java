package com.example.user_management_system.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Không tìm thấy User với id: " + id);
    }
}