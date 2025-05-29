package com.finance.tracker.service;

import com.finance.tracker.entity.Budget;
import com.finance.tracker.mapper.BudgetMapper;
import com.finance.tracker.model.BudgetDTO;
import com.finance.tracker.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public List<BudgetDTO> getBudgetsByUserId(Long userId) {
        List<Budget> budgets = budgetRepository.findByUserId(userId);
        return budgets.stream()
                .map(BudgetMapper::toDTO).collect(Collectors.toList());
    }

    public Budget findById(Long userId, Integer id) {
        return budgetRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
    }

    public Budget save(Budget budget) {
        return budgetRepository.save(budget);
    }

    public Budget updateBudget(Integer id, Budget updated, Long userId) {
        Budget existing = budgetRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
        existing.setAmount(updated.getAmount());
        existing.setMonth(updated.getMonth());
        existing.setCategory(updated.getCategory());
        return budgetRepository.save(existing);
    }

    public void deleteBudget(Integer id, Long userId) {
        Budget budget = budgetRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
        budgetRepository.deleteById(budget.getId());
    }
}
