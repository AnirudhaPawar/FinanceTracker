package com.finance.tracker.repository;

import com.finance.tracker.entity.Transaction;
import com.finance.tracker.dto.CategoryMonthlySummaryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>,
        JpaSpecificationExecutor<Transaction> {
    List<Transaction> findByUserId(Long userId);
    List<Transaction> findByUserIdAndCategoryId(Long user_id, Integer category_id);
    List<Transaction> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
    Optional<Transaction> findByIdAndUserId(Long transactionId, Long userId);

    @Query("SELECT new com.finance.tracker.dto.CategoryMonthlySummaryDTO(t.category.name, SUM(t.amount)) " +
            "FROM Transaction t " +
            "WHERE t.user.id = :userId AND MONTH(t.createdAt) = :month AND YEAR(t.createdAt) = :year " +
            "GROUP BY t.category.name")
    List<CategoryMonthlySummaryDTO> getCategorySummaryByMonth(@Param("userId") Long userId,
                                                              @Param("month") int month,
                                                              @Param("year") int year);

    @Query("SELECT t.category.id, SUM(t.amount) FROM Transaction t " +
            "WHERE t.user.id = :userId AND MONTH(t.createdAt) = :month AND YEAR(t.createdAt) = :year " +
            "GROUP BY t.category.id")
    List<Object[]> getTotalSpentPerCategory(@Param("userId") Long userId,
                                            @Param("month") int month,
                                            @Param("year") int year);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.category.name != 'Salary' AND t.createdAt >= :start AND t.createdAt < :end")
    BigDecimal getMonthlyTotal(@Param("userId") Long userId,
                               @Param("start") LocalDateTime start,
                               @Param("end") LocalDateTime end);

    @Query("SELECT t.category.name FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.createdAt >= :start AND t.createdAt < :end " +
            "GROUP BY t.category.name ORDER BY SUM(t.amount) DESC")
    List<String> getTopSpendingCategory(@Param("userId") Long userId,
                                        @Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.createdAt >= :start AND t.createdAt < :end")
    BigDecimal getTotalBetweenDates(@Param("userId") Long userId,
                                    @Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end);



}
