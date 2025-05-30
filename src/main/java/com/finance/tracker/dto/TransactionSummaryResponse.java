package com.finance.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionSummaryResponse {
    private double totalIncome;
    private double totalExpense;
    private double netSavings;
    private Map<String, Double> categoryWiseBreakdown;
}
