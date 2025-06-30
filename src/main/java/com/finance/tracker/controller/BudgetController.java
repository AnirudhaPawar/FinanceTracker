package com.finance.tracker.controller;

import com.finance.tracker.annotation.Authenticated;
import com.finance.tracker.dto.BudgetVsActualDTO;
import com.finance.tracker.entity.Budget;
import com.finance.tracker.entity.Category;
import com.finance.tracker.entity.User;
import com.finance.tracker.mapper.BudgetMapper;
import com.finance.tracker.dto.BudgetDTO;
import com.finance.tracker.service.BudgetService;
import com.finance.tracker.service.CategoryService;
import com.finance.tracker.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;
    private final CategoryService categoryService;
    private final BudgetMapper budgetMapper;
    private final CurrentUserUtil userUtil;

    @Authenticated
    @GetMapping
    public List<BudgetDTO> getBudgets() {
        User user = userUtil.getCurrentUser();
        return budgetService.getBudgetsByUserId(user.getId());
    }

    @Authenticated
    @GetMapping("/by-month")
    public ResponseEntity<List<BudgetDTO>> getBudgetsByMonth(
            @RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        User user = userUtil.getCurrentUser();
        List<Budget> budgets = budgetService.findByUserIdAndMonth(user.getId(), month);
        List<BudgetDTO> budgetDTOs = budgets.stream()
                .map(BudgetMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(budgetDTOs);
    }

    @Authenticated
    @GetMapping("/{id}")
    public ResponseEntity<BudgetDTO> getBudget(@PathVariable Integer id) {
        User user = userUtil.getCurrentUser();
        Budget budget = budgetService.findById(user.getId(), id);
        return ResponseEntity.ok(BudgetMapper.toDTO(budget));
    }

    @Authenticated
    @PostMapping
    public ResponseEntity<BudgetDTO> createBudget(@RequestBody BudgetDTO budgetDTO) {

        User user = userUtil.getCurrentUser();

        Category category = null;
        if (budgetDTO.getCategoryId() != null) {
            category = categoryService.findById(budgetDTO.getCategoryId());
        }

        Budget budget = budgetMapper.toEntity(budgetDTO, user, category);
        Budget saved = budgetService.save(budget);
        return ResponseEntity.ok(BudgetMapper.toDTO(saved));
    }

    @Authenticated
    @PutMapping("/{id}")
    public ResponseEntity<BudgetDTO> updateBudget(
                                                  @PathVariable Integer id,
                                                  @RequestBody BudgetDTO budgetDTO) {
        User user = userUtil.getCurrentUser();

        Category category = null;
        if (budgetDTO.getCategoryId() != null) {
            category = categoryService.findById(budgetDTO.getCategoryId());
        }

        Budget updatedBudget = budgetMapper.toEntity(budgetDTO, user, category);
        Budget saved = budgetService.updateBudget(id, updatedBudget, user.getId());

        return ResponseEntity.ok(BudgetMapper.toDTO(saved));
    }


    @Authenticated
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Integer id) {
        User user = userUtil.getCurrentUser();
        budgetService.deleteBudget(id, user.getId());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/summary")
    public ResponseEntity<List<BudgetVsActualDTO>> getBudgetSummary(
            @RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        Long userId = userUtil.getCurrentUser().getId();
        List<BudgetVsActualDTO> summary = budgetService.getBudgetVsActualSummary(userId, month);
        return ResponseEntity.ok(summary);
    }

}
