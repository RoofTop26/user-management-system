package com.example.user_management_system.exception;

public class InvalidResetTokenException extends RuntimeException {
    public InvalidResetTokenException() {
        super("Token không hợp lệ hoặc đã hết hạn");
    }
}
