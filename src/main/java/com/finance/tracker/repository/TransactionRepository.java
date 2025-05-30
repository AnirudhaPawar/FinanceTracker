package com.finance.tracker.repository;

import com.finance.tracker.entity.Transaction;
import com.finance.tracker.model.CategoryMonthlySummaryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);
    List<Transaction> findByUserIdAndCategoryId(Long user_id, Integer category_id);
    List<Transaction> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT new com.finance.tracker.model.CategoryMonthlySummaryDTO(t.category.name, SUM(t.amount)) " +
            "FROM Transaction t " +
            "WHERE t.user.id = :userId AND MONTH(t.createdAt) = :month AND YEAR(t.createdAt) = :year " +
            "GROUP BY t.category.name")
    List<CategoryMonthlySummaryDTO> getCategorySummaryByMonth(@Param("userId") Long userId,
                                                              @Param("month") int month,
                                                              @Param("year") int year);

}
