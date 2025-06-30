package com.finance.tracker.repository;

import com.finance.tracker.entity.RecurringTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RecurringTransactionRepository extends JpaRepository<RecurringTransaction, Long> {
    List<RecurringTransaction> findByActiveTrueAndNextDueDateLessThanEqual(LocalDate today);
    List<RecurringTransaction> findByUserId(Long userId);
}
