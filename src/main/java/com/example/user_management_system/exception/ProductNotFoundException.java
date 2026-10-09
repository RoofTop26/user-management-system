package com.example.user_management_system.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Không tìm thấy Product với id: " + id);
    }
}
