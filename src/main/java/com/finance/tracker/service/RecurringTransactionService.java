package com.finance.tracker.service;

import com.finance.tracker.dto.RecurringTransactionDTO;
import com.finance.tracker.entity.RecurringTransaction;
import com.finance.tracker.mapper.RecurringTransactionMapper;
import com.finance.tracker.repository.RecurringTransactionRepository;
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

    public Optional<RecurringTransaction> findById(Long id) {
        return repository.findById(id);
    }

    public RecurringTransactionDTO save(RecurringTransaction rt) {
        RecurringTransaction recurringTransaction = repository.save(rt);
        return RecurringTransactionMapper.toDto(recurringTransaction);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
