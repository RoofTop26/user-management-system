package com.example.user_management_system.controller;

import com.example.user_management_system.dto.response.CategoryResponse;
import com.example.user_management_system.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portal/categories")
public class PublicCategoryController {

    private final CategoryService categoryService;

    public PublicCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getActiveCategories() {
        return ResponseEntity.ok(categoryService.getActiveCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getActiveCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getActiveCategoryById(id));
    }
}
