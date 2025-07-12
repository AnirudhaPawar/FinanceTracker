package com.finance.tracker.service;

import com.finance.tracker.dto.BudgetVsActualDTO;
import com.finance.tracker.dto.BudgetDTO;
import com.finance.tracker.entity.Budget;
import com.finance.tracker.entity.Category;
import com.finance.tracker.mapper.BudgetMapper;
import com.finance.tracker.repository.BudgetRepository;
import com.finance.tracker.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BudgetServiceTest {

    @InjectMocks
    private BudgetService budgetService;

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBudgetsByUserId() {
        Long userId = 1L;
        Budget budget = new Budget();
        budget.setId(100);
        List<Budget> budgets = List.of(budget);

        when(budgetRepository.findByUserId(userId)).thenReturn(budgets);
        try (MockedStatic<BudgetMapper> mapperMockedStatic = mockStatic(BudgetMapper.class)) {
            BudgetDTO dto = new BudgetDTO();
            mapperMockedStatic.when(() -> BudgetMapper.toDTO(budget)).thenReturn(dto);

            List<BudgetDTO> result = budgetService.getBudgetsByUserId(userId);

            assertEquals(1, result.size());
            assertSame(dto, result.get(0));
        }

        verify(budgetRepository).findByUserId(userId);
    }

    @Test
    void testFindById_Found() {
        Long userId = 1L;
        Integer budgetId = 10;
        Budget budget = new Budget();

        when(budgetRepository.findByIdAndUserId(budgetId, userId)).thenReturn(Optional.of(budget));

        Budget result = budgetService.findById(userId, budgetId);

        assertNotNull(result);
        assertEquals(budget, result);
        verify(budgetRepository).findByIdAndUserId(budgetId, userId);
    }

    @Test
    void testFindById_NotFound() {
        Long userId = 1L;
        Integer budgetId = 10;

        when(budgetRepository.findByIdAndUserId(budgetId, userId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> budgetService.findById(userId, budgetId));
        assertEquals("Budget not found", ex.getMessage());

        verify(budgetRepository).findByIdAndUserId(budgetId, userId);
    }

    @Test
    void testSave() {
        Budget budget = new Budget();
        when(budgetRepository.save(budget)).thenReturn(budget);

        Budget saved = budgetService.save(budget);

        assertEquals(budget, saved);
        verify(budgetRepository).save(budget);
    }

    @Test
    void testUpdateBudget_Success() {
        Long userId = 1L;
        Integer budgetId = 5;
        Budget existing = new Budget();
        existing.setAmount(BigDecimal.valueOf(100));
        existing.setMonth("2023-07");
        existing.setCategory(new Category());

        Budget updated = new Budget();
        updated.setAmount(BigDecimal.valueOf(200));
        updated.setMonth("2023-08");
        Category newCategory = new Category();
        newCategory.setId(2);
        updated.setCategory(newCategory);

        when(budgetRepository.findByIdAndUserId(budgetId, userId)).thenReturn(Optional.of(existing));
        when(budgetRepository.save(existing)).thenReturn(existing);

        Budget result = budgetService.updateBudget(budgetId, updated, userId);

        assertEquals(updated.getAmount(), result.getAmount());
        assertEquals(updated.getMonth(), result.getMonth());
        assertEquals(updated.getCategory(), result.getCategory());
        verify(budgetRepository).findByIdAndUserId(budgetId, userId);
        verify(budgetRepository).save(existing);
    }

    @Test
    void testUpdateBudget_NotFound() {
        Long userId = 1L;
        Integer budgetId = 5;
        Budget updated = new Budget();

        when(budgetRepository.findByIdAndUserId(budgetId, userId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> budgetService.updateBudget(budgetId, updated, userId));
        assertEquals("Budget not found", ex.getMessage());

        verify(budgetRepository).findByIdAndUserId(budgetId, userId);
        verify(budgetRepository, never()).save(any());
    }

    @Test
    void testDeleteBudget_Success() {
        Long userId = 1L;
        Integer budgetId = 7;
        Budget budget = new Budget();
        budget.setId(7);

        when(budgetRepository.findByIdAndUserId(budgetId, userId)).thenReturn(Optional.of(budget));
        doNothing().when(budgetRepository).deleteById(budget.getId());

        budgetService.deleteBudget(budgetId, userId);

        verify(budgetRepository).findByIdAndUserId(budgetId, userId);
        verify(budgetRepository).deleteById(budget.getId());
    }

    @Test
    void testDeleteBudget_NotFound() {
        Long userId = 1L;
        Integer budgetId = 7;

        when(budgetRepository.findByIdAndUserId(budgetId, userId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> budgetService.deleteBudget(budgetId, userId));
        assertEquals("Budget not found", ex.getMessage());

        verify(budgetRepository).findByIdAndUserId(budgetId, userId);
        verify(budgetRepository, never()).deleteById(any());
    }

    @Test
    void testFindByUserIdAndMonth() {
        Long userId = 1L;
        YearMonth month = YearMonth.of(2023, 7);
        String monthValue = month.toString();
        List<Budget> budgets = List.of(new Budget());

        when(budgetRepository.findByUserIdAndMonth(userId, monthValue)).thenReturn(budgets);

        List<Budget> result = budgetService.findByUserIdAndMonth(userId, month);

        assertEquals(budgets, result);
        verify(budgetRepository).findByUserIdAndMonth(userId, monthValue);
    }

    @Test
    void testGetBudgetVsActualSummary() {
        Long userId = 1L;
        YearMonth month = YearMonth.of(2023, 7);
        String monthValue = month.toString();

        Category category = new Category();
        category.setId(10);
        category.setName("Food");

        Budget budget = new Budget();
        budget.setCategory(category);
        budget.setAmount(BigDecimal.valueOf(500));
        budget.setMonth(monthValue);

        List<Budget> budgets = List.of(budget);
        when(budgetRepository.findByUserIdAndMonth(userId, monthValue)).thenReturn(budgets);

        // Simulate transactionRepository.getTotalSpentPerCategory returning List<Object[]> with categoryId and amount
        List<Object[]> spentData = List.<Object[]>of(new Object[]{10, BigDecimal.valueOf(300)});

        when(transactionRepository.getTotalSpentPerCategory(userId, month.getMonthValue(), month.getYear())).thenReturn(spentData);

        List<BudgetVsActualDTO> result = budgetService.getBudgetVsActualSummary(userId, month);

        assertEquals(1, result.size());

        BudgetVsActualDTO dto = result.get(0);
        assertEquals("Food", dto.getCategory());
        assertEquals(500.0, dto.getBudgeted());
        assertEquals(300.0, dto.getSpent());
        assertEquals(200.0, dto.getRemaining());
        assertEquals("Within Budget", dto.getStatus());

        verify(budgetRepository).findByUserIdAndMonth(userId, monthValue);
        verify(transactionRepository).getTotalSpentPerCategory(userId, month.getMonthValue(), month.getYear());
    }
}
