package com.finance.tracker.service;

import com.finance.tracker.entity.Transaction;
import com.finance.tracker.entity.TransactionType;
import com.finance.tracker.model.TransactionDTO;
import com.finance.tracker.model.TransactionSummaryResponse;
import com.finance.tracker.repository.TransactionRepository;
import com.finance.tracker.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<TransactionDTO> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(MapperUtil::toTransactionDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> getAllTransactionsByUser(Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        return transactions.stream()
                .map(MapperUtil::toTransactionDTO)
                .collect(Collectors.toList());
    }

    public TransactionSummaryResponse getTransactionSummary(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = transactionRepository.findByUserIdAndCreatedAtBetween(userId, startDate, endDate);

        double totalIncome = 0;
        double totalExpense = 0;
        Map<String, Double> categoryBreakdown = new HashMap<>();

        for (Transaction tx : transactions) {
            String categoryName = tx.getCategory().getName();
            double amount = tx.getAmount().doubleValue();

            if (tx.getType() == TransactionType.income) {
                totalIncome += amount;
            } else if (tx.getType() == TransactionType.expense) {
                totalExpense += amount;
                categoryBreakdown.merge(categoryName, amount, Double::sum);
            }
        }

        TransactionSummaryResponse response = new TransactionSummaryResponse();
        response.setTotalIncome(totalIncome);
        response.setTotalExpense(totalExpense);
        response.setNetSavings(totalIncome - totalExpense);
        response.setCategoryWiseBreakdown(categoryBreakdown);

        return response;
    }
}
