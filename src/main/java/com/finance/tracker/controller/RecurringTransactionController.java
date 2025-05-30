package com.finance.tracker.controller;

import com.finance.tracker.dto.RecurringTransactionDTO;
import com.finance.tracker.entity.Category;
import com.finance.tracker.entity.RecurringTransaction;
import com.finance.tracker.entity.User;
import com.finance.tracker.mapper.RecurringTransactionMapper;
import com.finance.tracker.service.RecurringTransactionService;
import com.finance.tracker.service.UserService;
import com.finance.tracker.service.CategoryService;
import com.finance.tracker.util.CurrentUserUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recurring-transactions")
@RequiredArgsConstructor
public class RecurringTransactionController {

    private final RecurringTransactionService service;
    private final CurrentUserUtil userUtil;
    private final CategoryService categoryService;


    @GetMapping
    public List<RecurringTransaction> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecurringTransaction> getById(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody RecurringTransactionDTO dto) {
        User user = userUtil.getCurrentUser();
        Category category = categoryService.findById(dto.getCategoryId());
        RecurringTransaction rt = RecurringTransactionMapper.toEntity(dto, user, category);
        return ResponseEntity.ok(service.save(rt));
    }
}
