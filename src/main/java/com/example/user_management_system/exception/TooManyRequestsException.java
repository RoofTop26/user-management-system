package com.example.user_management_system.exception;

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException() {
        super("Vui lòng thử lại sau");
    }
}
