package com.example.user_management_system.exception;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException() {
        super("Sai username hoặc password");
    }
}