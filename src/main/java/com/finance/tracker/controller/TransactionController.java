package com.finance.tracker.controller;

import com.finance.tracker.annotation.Authenticated;
import com.finance.tracker.entity.Transaction;
import com.finance.tracker.entity.User;
import com.finance.tracker.model.TransactionDTO;
import com.finance.tracker.model.TransactionSummaryResponse;
import com.finance.tracker.service.TransactionService;
import com.finance.tracker.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final CurrentUserUtil userUtil;

    @PostMapping
    public Transaction saveTransaction(@RequestBody Transaction transaction) {
        return transactionService.saveTransaction(transaction);
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
}
