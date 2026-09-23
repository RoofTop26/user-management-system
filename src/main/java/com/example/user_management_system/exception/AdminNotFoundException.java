package com.example.user_management_system.exception;

public class AdminNotFoundException extends RuntimeException {
    public AdminNotFoundException(Long id) {
        super("Không tìm thấy Admin với id: " + id);
    }
}
