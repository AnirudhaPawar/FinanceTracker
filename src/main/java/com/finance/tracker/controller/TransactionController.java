package com.finance.tracker.controller;

import com.finance.tracker.annotation.Authenticated;
import com.finance.tracker.entity.Transaction;
import com.finance.tracker.model.TransactionDTO;
import com.finance.tracker.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public Transaction saveTransaction(@RequestBody Transaction transaction) {
        return transactionService.saveTransaction(transaction);
    }

    @GetMapping
    @Authenticated
    public List<TransactionDTO> getAllTransactions() {
        return transactionService.getAllTransactions();
    }
}
