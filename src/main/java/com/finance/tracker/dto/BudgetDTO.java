package com.finance.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetDTO {
    private Integer id;
    private Integer userId;
    private Integer categoryId;
    private CategoryDTO category;
    private String month;
    private BigDecimal amount;
}

