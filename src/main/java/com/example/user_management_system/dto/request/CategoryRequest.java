package com.example.user_management_system.dto.request;

import com.example.user_management_system.entity.Category;
import jakarta.validation.constraints.NotBlank;

public class CategoryRequest {

    @NotBlank(message = "name không được để trống")
    private String name;

    private String description;

    private Category.Status status;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Category.Status getStatus() { return status; }
    public void setStatus(Category.Status status) { this.status = status; }
}
