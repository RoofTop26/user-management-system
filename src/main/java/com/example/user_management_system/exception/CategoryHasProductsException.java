package com.example.user_management_system.exception;

public class CategoryHasProductsException extends RuntimeException {
    public CategoryHasProductsException() {
        super("Không thể xóa danh mục đang có sản phẩm");
    }
}
