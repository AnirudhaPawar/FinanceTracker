package com.finance.tracker.util;

import com.finance.tracker.entity.Transaction;
import com.finance.tracker.model.TransactionDTO;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class MapperUtil {

    public static TransactionDTO toTransactionDTO(Transaction t) {
        return new TransactionDTO(
                t.getId(),
                t.getAmount(),
                nonNull(t.getCategory()) ? t.getCategory() : null,
                t.getCreatedAt(),
                t.getUser().getId(),
                t.getType(),
                t.getNote() != null ? t.getNote() : EMPTY
        );
    }

}

