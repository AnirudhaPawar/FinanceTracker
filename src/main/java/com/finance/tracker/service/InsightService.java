package com.finance.tracker.service;

import com.finance.tracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InsightService {

    private final TransactionRepository transactionRepository;

    public List<String> generateInsights(Long userId) {
        List<String> insights = new ArrayList<>();

        // 1. Monthly spend comparison
        YearMonth currentMonth = YearMonth.now();
        YearMonth lastMonth = currentMonth.minusMonths(1);
        LocalDateTime thisMonthStart = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime thisMonthEnd = currentMonth.plusMonths(1).atDay(1).atStartOfDay();
        //LocalDate thisMonthStart = currentMonth.atDay(1);

        LocalDateTime start = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime end = currentMonth.plusMonths(1).atDay(1).atStartOfDay();
        BigDecimal thisMonthTotal = transactionRepository.getMonthlyTotal(userId, start, end);

        LocalDateTime lastStart = lastMonth.atDay(1).atStartOfDay();
        LocalDateTime lastEnd = currentMonth.atDay(1).atStartOfDay();
        BigDecimal lastMonthTotal = transactionRepository.getMonthlyTotal(userId, lastStart, lastEnd);
        BigDecimal difference = thisMonthTotal.subtract(lastMonthTotal);

        if (difference.compareTo(BigDecimal.ZERO) > 0) {
            insights.add("You spent ₹" + difference + " more than last month.");
        } else if (difference.compareTo(BigDecimal.ZERO) < 0) {
            insights.add("You saved ₹" + difference.abs() + " compared to last month.");
        } else {
            insights.add("Your spending is the same as last month.");
        }

        // 2. Top spending category this month
        LocalDate now = LocalDate.now();
        List<String> topCategories = transactionRepository.getTopSpendingCategory(userId, thisMonthStart, thisMonthEnd);
        if (!topCategories.isEmpty()) {
            insights.add("Top spending category this month: " + topCategories.get(0));
        }

        // 3. Average daily spending in last 7 days
        LocalDateTime weekStart = now.minusDays(6).atStartOfDay();
        LocalDateTime weekEnd = now.plusDays(1).atStartOfDay(); // to include today fully

        BigDecimal weeklyTotal = transactionRepository.getTotalBetweenDates(userId, weekStart, weekEnd);
        BigDecimal avgPerDay = weeklyTotal.divide(BigDecimal.valueOf(7), 2, RoundingMode.HALF_UP);
        insights.add("Your average daily expense this week is ₹" + avgPerDay);

        return insights;
    }
}

