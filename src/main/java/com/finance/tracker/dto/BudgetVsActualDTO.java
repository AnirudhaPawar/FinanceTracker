package com.finance.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BudgetVsActualDTO {
    private String category;
    private double budgeted;
    private double spent;
    private double remaining;
    private String status;
}
