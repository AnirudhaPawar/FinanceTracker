package com.finance.tracker.service;

import com.finance.tracker.dto.PagedResponse;
import com.finance.tracker.entity.Transaction;
import com.finance.tracker.entity.TransactionType;
import com.finance.tracker.entity.User;
import com.finance.tracker.mapper.TransactionMapper;
import com.finance.tracker.dto.CategoryMonthlySummaryDTO;
import com.finance.tracker.dto.TransactionDTO;
import com.finance.tracker.dto.TransactionSummaryResponse;
import com.finance.tracker.repository.TransactionRepository;
import com.finance.tracker.specification.TransactionSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionDTO saveTransaction(TransactionDTO transactionDTO, User user) {
        Transaction transaction = transactionMapper.toTransactionEntity(transactionDTO, user);
        transaction = transactionRepository.save(transaction);
        return transactionMapper.toTransactionDTO(transaction);
    }

    public List<TransactionDTO> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(transactionMapper::toTransactionDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> getAllTransactionsByUser(Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        return transactions.stream()
                .map(transactionMapper::toTransactionDTO)
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

    public List<CategoryMonthlySummaryDTO> getMonthlyCategorySummary(Long userId, YearMonth month) {
        return transactionRepository.getCategorySummaryByMonth(
                userId, month.getMonthValue(), month.getYear()
        );
    }

    public PagedResponse<TransactionDTO> getFilteredTransactions(
            Long userId, Long categoryId, TransactionType type,
            LocalDate startDate, LocalDate endDate,
            int page, int size, String sortBy, String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Transaction> spec = TransactionSpecification.withFilters(
                userId, categoryId, type, startDate, endDate
        );

        Page<Transaction> transactionPage = transactionRepository.findAll(spec, pageable);
        Page<TransactionDTO> dtoPage = transactionPage.map(transactionMapper::toTransactionDTO);

        return new PagedResponse<>(
                dtoPage.getContent(),
                dtoPage.getNumber(),
                dtoPage.getSize(),
                dtoPage.getTotalElements(),
                dtoPage.getTotalPages(),
                dtoPage.isLast()
        );
    }
}
