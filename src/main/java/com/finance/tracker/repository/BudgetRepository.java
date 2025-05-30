package com.finance.tracker.repository;

import com.finance.tracker.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    List<Budget> findByUserId(Long userId);
    Optional<Budget> findByIdAndUserId(Integer id, Long userId);

    @Query("SELECT b FROM Budget b WHERE b.user.id = :userId AND b.month = :month")
    List<Budget> findByUserIdAndMonth(@Param("userId") Long userId, @Param("month") String month);

}

