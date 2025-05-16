package com.finance.tracker.util;

import com.finance.tracker.entity.Transaction;
import com.finance.tracker.entity.User;
import com.finance.tracker.model.TransactionDTO;
import com.finance.tracker.model.UserDTO;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class MapperUtil {

    public static TransactionDTO toTransactionDTO(Transaction t) {
        return new TransactionDTO(
                t.getId(),
                t.getAmount(),
                nonNull(t.getCategory()) ? t.getCategory().getName() : EMPTY,
                t.getCreatedAt(),
                t.getUser().getId()
        );
    }

    public static UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}

