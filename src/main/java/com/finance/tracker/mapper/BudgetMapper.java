package com.finance.tracker.mapper;

import com.finance.tracker.entity.Budget;
import com.finance.tracker.entity.Category;
import com.finance.tracker.entity.User;
import com.finance.tracker.dto.BudgetDTO;
import com.finance.tracker.dto.CategoryDTO;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
public class BudgetMapper {

    public static BudgetDTO toDTO(Budget budget) {
        if (isNull(budget)) return null;

        CategoryDTO categoryDTO = nonNull(budget.getCategory()) ? CategoryDTO.builder().id(Math.toIntExact(budget.getCategory().getId())).name(budget.getCategory().getName()).build() : null;

        BudgetDTO dto = new BudgetDTO();
        dto.setId(budget.getId());
        dto.setUserId(Math.toIntExact(budget.getUser().getId()));
        dto.setCategory(categoryDTO);
        dto.setCategoryId(nonNull(categoryDTO) ? categoryDTO.getId() : null);
        dto.setMonth(budget.getMonth());
        dto.setAmount(budget.getAmount());

        return dto;
    }

    public Budget toEntity(BudgetDTO dto, User user, Category category) {
        if (isNull(dto)) return null;

        Budget budget = new Budget();
        budget.setId(dto.getId());
        budget.setUser(user);
        budget.setCategory(category);
        budget.setMonth(dto.getMonth());
        budget.setAmount(dto.getAmount());

        return budget;
    }
}

