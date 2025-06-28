package com.finance.tracker.controller;

import com.finance.tracker.annotation.Authenticated;
import com.finance.tracker.dto.PagedResponse;
import com.finance.tracker.entity.TransactionType;
import com.finance.tracker.entity.User;
import com.finance.tracker.dto.CategoryMonthlySummaryDTO;
import com.finance.tracker.dto.TransactionDTO;
import com.finance.tracker.dto.TransactionSummaryResponse;
import com.finance.tracker.service.TransactionService;
import com.finance.tracker.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final CurrentUserUtil userUtil;

    @PostMapping
    public TransactionDTO saveTransaction(@RequestBody TransactionDTO transaction) {
        User user = userUtil.getCurrentUser();
        return transactionService.saveTransaction(transaction, user);
    }

    @PutMapping("/{transactionId}")
    public TransactionDTO editTransaction(
            @PathVariable Long transactionId,
            @RequestBody TransactionDTO updatedTransaction
    ) {
        User user = userUtil.getCurrentUser();
        return transactionService.editTransaction(transactionId, updatedTransaction, user);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long transactionId) {
        User user = userUtil.getCurrentUser();
        transactionService.deleteTransaction(transactionId, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Authenticated
    public List<TransactionDTO> getAllTransactions() {
        User user = userUtil.getCurrentUser();
        return transactionService.getAllTransactionsByUser(user.getId());
    }

    @GetMapping("/summary")
    public ResponseEntity<TransactionSummaryResponse> getSummary(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        User user = userUtil.getCurrentUser();
        TransactionSummaryResponse summary = transactionService.getTransactionSummary(user.getId(), start, end);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/category-summary")
    public ResponseEntity<List<CategoryMonthlySummaryDTO>> getCategoryMonthlySummary(
            @RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        Long userId = userUtil.getCurrentUser().getId();
        List<CategoryMonthlySummaryDTO> summary = transactionService.getMonthlyCategorySummary(userId, month);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/paged")
    public ResponseEntity<PagedResponse<TransactionDTO>> getTransactionsPaginated(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) String note,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Long userId = userUtil.getCurrentUser().getId();

        PagedResponse<TransactionDTO> transactions = transactionService.getFilteredTransactions(
                userId, categoryId, type,  note, tag, startDate, endDate, page, size, sortBy, sortDir
        );

        return ResponseEntity.ok(transactions);
    }


}
