package com.finance.tracker.dto;

import com.finance.tracker.entity.Frequency;
import com.finance.tracker.entity.TransactionType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecurringTransactionDTO {

    private Long userId;

    @NotNull
    private Integer categoryId;

    @NotNull
    private TransactionType type;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    private String note;

    @NotNull
    private Frequency frequency;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    // Optional: if null, use startDate
    private LocalDate nextDueDate;

    private Boolean active = true;
}
