package com.finance.tracker.mapper;

import com.finance.tracker.entity.Category;
import com.finance.tracker.entity.Transaction;
import com.finance.tracker.entity.User;
import com.finance.tracker.exception.ResourceNotFoundException;
import com.finance.tracker.model.CategoryDTO;
import com.finance.tracker.model.TransactionDTO;
import com.finance.tracker.repository.CategoryRepository;
import com.finance.tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public TransactionDTO toTransactionDTO(Transaction t) {
        return new TransactionDTO(
                t.getAmount(),
                nonNull(t.getCategory()) ? CategoryDTO.builder().id(Math.toIntExact(t.getCategory().getId())).name(t.getCategory().getName()).build() : null,
                t.getCreatedAt(),
                t.getUser().getId(),
                t.getType(),
                t.getNote() != null ? t.getNote() : EMPTY
        );
    }

    public Transaction toTransactionEntity(TransactionDTO dto, User user) {

        Transaction transaction = new Transaction();

        Category category = categoryRepository.findById(Long.valueOf(dto.getCategory().getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        transaction.setCategory(category);
        transaction.setUser(user);
        transaction.setAmount(dto.getAmount());
        transaction.setCreatedAt(dto.getCreatedDate());
        transaction.setType(dto.getType());
        transaction.setNote(dto.getNote());
        return transaction;
    }


}


