package com.finance.tracker.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "finance_db", name = "transactions")
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Foreign key to users table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_transaction_user"))
    private User user;

    // Foreign key to categories table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_transaction_category"))
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 10, nullable = false)
    private TransactionType type;

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ElementCollection
    @CollectionTable(schema = "finance_db", name = "transaction_tags", joinColumns = @JoinColumn(name = "transaction_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();


    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
