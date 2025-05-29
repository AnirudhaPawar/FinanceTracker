package com.finance.tracker.repository;

import com.finance.tracker.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    List<Budget> findByUserId(Long userId);
    Optional<Budget> findByIdAndUserId(Integer id, Long userId);
}

