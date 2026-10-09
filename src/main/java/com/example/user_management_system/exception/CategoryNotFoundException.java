package com.example.user_management_system.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("Không tìm thấy Category với id: " + id);
    }
}
