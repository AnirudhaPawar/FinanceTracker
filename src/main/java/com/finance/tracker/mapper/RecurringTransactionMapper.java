package com.finance.tracker.mapper;

import com.finance.tracker.dto.RecurringTransactionDTO;
import com.finance.tracker.entity.Category;
import com.finance.tracker.entity.RecurringTransaction;
import com.finance.tracker.entity.User;

import java.time.LocalDateTime;

public class RecurringTransactionMapper {

    public static RecurringTransaction toEntity(RecurringTransactionDTO dto, User user, Category category) {
        RecurringTransaction rt = new RecurringTransaction();
        rt.setUser(user);
        rt.setCategory(category);
        rt.setType(dto.getType());
        rt.setAmount(dto.getAmount());
        rt.setNote(dto.getNote());
        rt.setFrequency(dto.getFrequency());
        rt.setStartDate(dto.getStartDate());
        rt.setEndDate(dto.getEndDate());
        rt.setNextDueDate(dto.getNextDueDate() != null ? dto.getNextDueDate() : dto.getStartDate());
        rt.setActive(dto.getActive() != null ? dto.getActive() : true);
        rt.setCreatedAt(LocalDateTime.now());
        return rt;
    }

    public static RecurringTransactionDTO toDto(RecurringTransaction entity) {
        RecurringTransactionDTO dto = new RecurringTransactionDTO();
        dto.setUserId(entity.getUser().getId());
        dto.setCategoryId(entity.getCategory().getId());
        dto.setType(entity.getType());
        dto.setAmount(entity.getAmount());
        dto.setNote(entity.getNote());
        dto.setFrequency(entity.getFrequency());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setNextDueDate(entity.getNextDueDate() != null ? entity.getNextDueDate() : entity.getStartDate());
        dto.setActive(entity.getActive() != null ? entity.getActive() : true);
        return dto;
    }
}
