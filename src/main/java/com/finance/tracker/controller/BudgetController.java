package com.finance.tracker.controller;

import com.finance.tracker.annotation.Authenticated;
import com.finance.tracker.entity.Budget;
import com.finance.tracker.entity.Category;
import com.finance.tracker.entity.User;
import com.finance.tracker.mapper.BudgetMapper;
import com.finance.tracker.model.BudgetDTO;
import com.finance.tracker.service.BudgetService;
import com.finance.tracker.service.CategoryService;
import com.finance.tracker.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    private final CustomUserDetailsService userDetailsService;

    private final CategoryService categoryService;

    private final BudgetMapper budgetMapper;

    @Authenticated
    @GetMapping
    public List<BudgetDTO> getBudgets(@RequestHeader("X-User-Id") String userId) {
        return budgetService.getBudgetsByUserId(Long.valueOf(userId));
    }

    @Authenticated
    @GetMapping("/{id}")
    public ResponseEntity<BudgetDTO> getBudget(@RequestHeader("X-User-Id") String userId, @PathVariable Integer id) {
        Budget budget = budgetService.findById(userId, id);
        return ResponseEntity.ok(budgetMapper.toDTO(budget));
    }

    @Authenticated
    @PostMapping
    public ResponseEntity<BudgetDTO> createBudget(@RequestHeader("X-User-Id") String userId, @RequestBody BudgetDTO budgetDTO) {

        User user = userDetailsService.findByUserId(Long.valueOf(userId));

        Category category = null;
        if (budgetDTO.getCategoryId() != null) {
            category = categoryService.findById(budgetDTO.getCategoryId());
        }

        Budget budget = budgetMapper.toEntity(budgetDTO, user, category);
        Budget saved = budgetService.save(budget);
        return ResponseEntity.ok(budgetMapper.toDTO(saved));
    }

    @Authenticated
    @PutMapping("/{id}")
    public ResponseEntity<BudgetDTO> updateBudget(@RequestHeader("X-User-Id") String userIdHeader,
                                                  @PathVariable Integer id,
                                                  @RequestBody BudgetDTO budgetDTO) {
        Long userId = Long.valueOf(userIdHeader);

        User user = userDetailsService.findByUserId(userId);

        Category category = null;
        if (budgetDTO.getCategoryId() != null) {
            category = categoryService.findById(budgetDTO.getCategoryId());
        }

        Budget updatedBudget = budgetMapper.toEntity(budgetDTO, user, category);
        Budget saved = budgetService.updateBudget(id, updatedBudget, userId);

        return ResponseEntity.ok(budgetMapper.toDTO(saved));
    }


    @Authenticated
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@RequestHeader("X-User-Id") String userId, @PathVariable Integer id) {
        budgetService.deleteBudget(id, Long.valueOf(userId));
        return ResponseEntity.ok().build();
    }
}
