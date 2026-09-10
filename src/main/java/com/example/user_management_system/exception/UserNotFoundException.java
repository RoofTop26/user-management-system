package com.example.user_management_system.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Không tìm thấy User với id: " + id);
    }

    public UserNotFoundException(String username) {
        super("Không tìm thấy User với username: " + username);
    }

}