package com.finance.tracker.service;

import com.finance.tracker.entity.Category;
import com.finance.tracker.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategories() {
        List<Category> categories = List.of(new Category(), new Category());
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        verify(categoryRepository).findAll();
    }

    @Test
    void testGetCategoryById_Found() {
        Category category = new Category();
        category.setId(1);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(categoryRepository).findById(1L);
    }

    @Test
    void testGetCategoryById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> categoryService.getCategoryById(1));
        assertEquals("Category not found", ex.getMessage());
        verify(categoryRepository).findById(1L);
    }

    @Test
    void testFindById_Found() {
        Category category = new Category();
        category.setId(2);
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));

        Category result = categoryService.findById(2);

        assertNotNull(result);
        assertEquals(2, result.getId());
        verify(categoryRepository).findById(2L);
    }

    @Test
    void testFindById_NotFound() {
        when(categoryRepository.findById(3L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> categoryService.findById(3));
        assertEquals("Category not found", ex.getMessage());
        verify(categoryRepository).findById(3L);
    }

    @Test
    void testCreateCategory() {
        Category category = new Category();
        category.setName("Food");

        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.createCategory(category);

        assertNotNull(result);
        assertEquals("Food", result.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    void testUpdateCategory_Success() {
        Category existingCategory = new Category();
        existingCategory.setId(1);
        existingCategory.setName("Old Name");

        Category updatedCategory = new Category();
        updatedCategory.setName("New Name");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(existingCategory);

        Category result = categoryService.updateCategory(1, updatedCategory);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(existingCategory);
    }

    @Test
    void testUpdateCategory_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> categoryService.updateCategory(1, new Category()));

        assertEquals("Category not found", ex.getMessage());
        verify(categoryRepository).findById(1L);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void testDeleteCategory() {
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteCategory(1);

        verify(categoryRepository).deleteById(1L);
    }
}
