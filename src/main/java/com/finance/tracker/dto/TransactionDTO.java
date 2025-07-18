package com.finance.tracker.dto;

import com.finance.tracker.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private Long id;
    private BigDecimal amount;
    private CategoryDTO category;
    private LocalDateTime createdDate;
    private Long userId;
    private TransactionType type;
    private String note;
    private Set<String> tags;
}
