package com.finance.tracker.specification;

import com.finance.tracker.entity.Transaction;
import com.finance.tracker.entity.TransactionType;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification {

    public static Specification<Transaction> withFilters(
            Long userId, Long categoryId, TransactionType type,
            String note, String tag,
            LocalDate startDate, LocalDate endDate
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userId != null) {
                Join<Object, Object> userJoin = root.join("user", JoinType.INNER);
                predicates.add(cb.equal(userJoin.get("id"), userId));
            }

            if (categoryId != null) {
                Join<Object, Object> categoryJoin = root.join("category", JoinType.LEFT); // Use LEFT if category is optional
                predicates.add(cb.equal(categoryJoin.get("id"), categoryId));
            }

            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }

            if (note != null && !note.isBlank())
                predicates.add(cb.like(cb.lower(root.get("note")), "%" + note.toLowerCase() + "%"));

            if (tag != null && !tag.isBlank()) {
                Join<Transaction, String> tagsJoin = root.joinSet("tags", JoinType.LEFT);
                predicates.add(cb.equal(tagsJoin, tag));
            }

            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), startDate.atStartOfDay()));
            }

            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), endDate.atTime(LocalTime.MAX)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


}
