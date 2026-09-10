package com.example.user_management_system.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super("Username đã tồn tại: " + username);
    }
}