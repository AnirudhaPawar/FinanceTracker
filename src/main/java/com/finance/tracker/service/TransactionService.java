package com.finance.tracker.service;

import com.finance.tracker.entity.Transaction;
import com.finance.tracker.model.TransactionDTO;
import com.finance.tracker.repository.TransactionRepository;
import com.finance.tracker.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
