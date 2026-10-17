package com.example.user_management_system.exception;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException(Long id) {
        super("Không tìm thấy CartItem với id: " + id);
    }
}
