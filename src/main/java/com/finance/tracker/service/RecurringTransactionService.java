package com.finance.tracker.service;

import com.finance.tracker.dto.RecurringTransactionDTO;
import com.finance.tracker.entity.RecurringTransaction;
import com.finance.tracker.mapper.RecurringTransactionMapper;
import com.finance.tracker.repository.RecurringTransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecurringTransactionService {

    private final RecurringTransactionRepository repository;

    public List<RecurringTransactionDTO> findAll() {
        return repository.findAll().stream().map(RecurringTransactionMapper::toDto).toList();
    }

    public RecurringTransactionDTO findById(Long id) {
        RecurringTransaction recurringTransaction = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        return RecurringTransactionMapper.toDto(recurringTransaction);
    }

    public List<RecurringTransactionDTO> findByUserId(Long id) {
        List<RecurringTransaction> recurringTransactions = repository.findByUserId(id);
        return recurringTransactions.stream().map(RecurringTransactionMapper::toDto).toList();
    }

    public RecurringTransactionDTO save(RecurringTransaction rt) {
        RecurringTransaction recurringTransaction = repository.save(rt);
        return RecurringTransactionMapper.toDto(recurringTransaction);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
