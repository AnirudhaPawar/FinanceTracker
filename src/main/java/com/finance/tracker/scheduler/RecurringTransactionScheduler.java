package com.finance.tracker.scheduler;

import com.finance.tracker.entity.Frequency;
import com.finance.tracker.entity.RecurringTransaction;
import com.finance.tracker.entity.Transaction;
import com.finance.tracker.repository.RecurringTransactionRepository;
import com.finance.tracker.repository.TransactionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class RecurringTransactionScheduler {

    private final RecurringTransactionRepository recurringRepo;
    private final TransactionRepository transactionRepo;

    public RecurringTransactionScheduler(RecurringTransactionRepository recurringRepo,
                                         TransactionRepository transactionRepo) {
        this.recurringRepo = recurringRepo;
        this.transactionRepo = transactionRepo;
    }

    @Scheduled(cron = "0 0 2 * * ?") // 2 AM daily
    public void generateRecurringTransactions() {
        LocalDate today = LocalDate.now();
        List<RecurringTransaction> dueTransactions = recurringRepo.findByActiveTrueAndNextDueDateLessThanEqual(today);

        for (RecurringTransaction rt : dueTransactions) {
            Transaction txn = new Transaction();
            txn.setUser(rt.getUser());
            txn.setCategory(rt.getCategory());
            txn.setAmount(rt.getAmount());
            txn.setNote(rt.getNote());
            txn.setType(rt.getType());
            txn.setCreatedAt(LocalDateTime.now());

            transactionRepo.save(txn);

            // Advance the next due date
            LocalDate nextDate = getNextDate(rt.getNextDueDate(), rt.getFrequency());
            rt.setNextDueDate(nextDate);

            // Deactivate if beyond end date
            if (rt.getEndDate() != null && nextDate.isAfter(rt.getEndDate())) {
                rt.setActive(false);
            }

            recurringRepo.save(rt);
        }
    }

    private LocalDate getNextDate(LocalDate current, Frequency frequency) {
        return switch (frequency) {
            case DAILY -> current.plusDays(1);
            case WEEKLY -> current.plusWeeks(1);
            case MONTHLY -> current.plusMonths(1);
            case YEARLY -> current.plusYears(1);
        };
    }
}
