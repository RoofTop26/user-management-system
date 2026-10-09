package com.example.user_management_system.service;

import com.example.user_management_system.dto.request.CategoryRequest;
import com.example.user_management_system.dto.response.CategoryResponse;
import com.example.user_management_system.entity.Category;
import com.example.user_management_system.exception.CategoryNotFoundException;
import com.example.user_management_system.exception.UsernameAlreadyExistsException;
import com.example.user_management_system.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public CategoryResponse getCategoryById(Long id) {
        return toResponse(findCategory(id));
    }

    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new UsernameAlreadyExistsException(request.getName());
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setStatus(request.getStatus() != null ? request.getStatus() : Category.Status.ACTIVE);
        return toResponse(categoryRepository.save(category));
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = findCategory(id);

        if (!category.getName().equals(request.getName())
                && categoryRepository.findByName(request.getName()).isPresent()) {
            throw new UsernameAlreadyExistsException(request.getName());
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setStatus(request.getStatus() != null ? request.getStatus() : category.getStatus());
        return toResponse(categoryRepository.save(category));
    }

    public CategoryResponse patchCategory(Long id, CategoryRequest request) {
        Category category = findCategory(id);

        if (request.getName() != null && !request.getName().isBlank()) {
            if (!category.getName().equals(request.getName())
                    && categoryRepository.findByName(request.getName()).isPresent()) {
                throw new UsernameAlreadyExistsException(request.getName());
            }
            category.setName(request.getName());
        }
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            category.setStatus(request.getStatus());
        }
        return toResponse(categoryRepository.save(category));
    }

    public void deleteCategory(Long id) {
        Category category = findCategory(id);
        categoryRepository.delete(category);
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getStatus(),
                category.getCreatedAt(),
                category.getUpdatedAt());
    }
}
