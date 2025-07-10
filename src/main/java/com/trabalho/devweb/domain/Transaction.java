package com.trabalho.devweb.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private String id;
    private String originId;
    private String targetId;
    private String type;
    private BigDecimal amount;
    private String description;
    private BigDecimal balanceAfter;
    private LocalDateTime createdAt;

    public Transaction(String id, String originId, String targetId, String type,
            BigDecimal amount, String description, BigDecimal balanceAfter,
            LocalDateTime createdAt) {
        this.id = id;
        this.originId = originId;
        this.targetId = targetId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.balanceAfter = balanceAfter;
        this.createdAt = createdAt;
    }

    public static Transaction create(String accountId, BigDecimal amount,
            String description, BigDecimal balanceAfter) {
        String id = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();
        return new Transaction(id, accountId, null, "INVESTMENT", amount, description, balanceAfter, createdAt);
    }

    public static Transaction createRedemption(String accountId, BigDecimal amount,
            String description, BigDecimal balanceAfter) {
        String id = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();
        return new Transaction(id, null, accountId, "REDEMPTION", amount, description, balanceAfter, createdAt);
    }

    public String getId() {
        return id;
    }

    public String getOriginId() {
        return originId;
    }

    public String getTargetId() {
        return targetId;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
