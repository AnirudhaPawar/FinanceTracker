package com.finance.tracker.service;

import com.finance.tracker.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class InsightServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private InsightService insightService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateInsights() {
        Long userId = 1L;

        YearMonth currentMonth = YearMonth.now();
        YearMonth lastMonth = currentMonth.minusMonths(1);

        LocalDateTime thisMonthStart = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime thisMonthEnd = currentMonth.plusMonths(1).atDay(1).atStartOfDay();

        LocalDateTime lastMonthStart = lastMonth.atDay(1).atStartOfDay();
        LocalDateTime lastMonthEnd = currentMonth.atDay(1).atStartOfDay();

        LocalDate now = LocalDate.now();
        LocalDateTime weekStart = now.minusDays(6).atStartOfDay();
        LocalDateTime weekEnd = now.plusDays(1).atStartOfDay();

        // Stub repository calls
        when(transactionRepository.getMonthlyTotal(userId, thisMonthStart, thisMonthEnd))
                .thenReturn(new BigDecimal("1500.00"));
        when(transactionRepository.getMonthlyTotal(userId, lastMonthStart, lastMonthEnd))
                .thenReturn(new BigDecimal("1000.00"));

        when(transactionRepository.getTopSpendingCategory(userId, thisMonthStart, thisMonthEnd))
                .thenReturn(List.of("Food", "Transport"));

        when(transactionRepository.getTotalBetweenDates(userId, weekStart, weekEnd))
                .thenReturn(new BigDecimal("700.00"));

        // Call method under test
        List<String> insights = insightService.generateInsights(userId);

        // Verify expected insights strings exist
        assertTrue(insights.stream().anyMatch(s -> s.contains("spent ₹500.00 more")));
        assertTrue(insights.stream().anyMatch(s -> s.contains("Top spending category this month: Food")));
        assertTrue(insights.stream().anyMatch(s -> s.contains("average daily expense this week is ₹100.00")));

        // Verify repository method calls
        verify(transactionRepository).getMonthlyTotal(userId, thisMonthStart, thisMonthEnd);
        verify(transactionRepository).getMonthlyTotal(userId, lastMonthStart, lastMonthEnd);
        verify(transactionRepository).getTopSpendingCategory(userId, thisMonthStart, thisMonthEnd);
        verify(transactionRepository).getTotalBetweenDates(userId, weekStart, weekEnd);
    }
}
