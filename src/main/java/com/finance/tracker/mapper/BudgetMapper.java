package com.finance.tracker.mapper;

import com.finance.tracker.entity.Budget;
import com.finance.tracker.entity.Category;
import com.finance.tracker.entity.User;
import com.finance.tracker.model.BudgetDTO;
import com.finance.tracker.model.CategoryDTO;
import org.springframework.stereotype.Component;

@Component
public class BudgetMapper {

    public static BudgetDTO toDTO(Budget budget) {
        if (budget == null) return null;

        BudgetDTO dto = new BudgetDTO();
        dto.setId(budget.getId());
        dto.setUserId(Math.toIntExact(budget.getUser().getId()));
        dto.setCategory(CategoryDTO.builder().id(Math.toIntExact(budget.getCategory().getId())).name(budget.getCategory().getName()).build());
        dto.setCategoryId(Math.toIntExact(budget.getCategory().getId()));
        dto.setMonth(budget.getMonth());
        dto.setAmount(budget.getAmount());

        return dto;
    }

    public Budget toEntity(BudgetDTO dto, User user, Category category) {
        if (dto == null) return null;

        Budget budget = new Budget();
        budget.setId(dto.getId());
        budget.setUser(user);
        budget.setCategory(category);
        budget.setMonth(dto.getMonth());
        budget.setAmount(dto.getAmount());

        return budget;
    }
}

