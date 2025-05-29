package com.finance.tracker.service;

import com.finance.tracker.entity.Category;
import com.finance.tracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category findById(Integer id) {
        return categoryRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Integer id, Category updatedCategory) {
        Category category = categoryRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(updatedCategory.getName());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(Long.valueOf(id));
    }
}
