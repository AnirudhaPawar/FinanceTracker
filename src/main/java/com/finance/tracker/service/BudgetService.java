package com.finance.tracker.service;

import com.finance.tracker.dto.BudgetVsActualDTO;
import com.finance.tracker.entity.Budget;
import com.finance.tracker.mapper.BudgetMapper;
import com.finance.tracker.dto.BudgetDTO;
import com.finance.tracker.repository.BudgetRepository;
import com.finance.tracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.apache.logging.log4j.util.Strings.EMPTY;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;

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

    public List<BudgetVsActualDTO> getBudgetVsActualSummary(Long userId, YearMonth month) {
        int year = month.getYear();
        String monthValue = month.toString();

        List<Budget> budgets = budgetRepository.findByUserIdAndMonth(userId, monthValue);

        Map<Integer, BigDecimal> spentMap = transactionRepository.getTotalSpentPerCategory(userId, month.getMonthValue(), year)
                .stream()
                .collect(Collectors.toMap(
                        obj -> (Integer) obj[0],
                        obj -> obj[1] != null ? (BigDecimal) obj[1] : new BigDecimal(0)
                ));

        List<BudgetVsActualDTO> result = new ArrayList<>();

        for (Budget budget : budgets) {
            String category = EMPTY;
            double spent = 0.0;
            if (nonNull(budget.getCategory())) {
                category = budget.getCategory().getName();
                spent = spentMap.getOrDefault(budget.getCategory().getId(), new BigDecimal(0)).toBigInteger().doubleValue();
            }
            double budgeted = budget.getAmount().toBigInteger().doubleValue();
            double remaining = budgeted - spent;
            String status = remaining >= 0 ? "Within Budget" : "Over Budget";

            result.add(new BudgetVsActualDTO(category, budgeted, spent, remaining, status));
        }

        return result;
    }

}
