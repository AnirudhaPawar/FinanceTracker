package com.finance.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CategoryMonthlySummaryDTO {
    private String categoryName;
    private BigDecimal totalAmount;
}
