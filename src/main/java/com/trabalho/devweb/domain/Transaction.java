package com.trabalho.devweb.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public static Transaction create(String originId, String targetId, String type,
            BigDecimal amount, String description, BigDecimal balanceAfter) {
        String id = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();
        return new Transaction(id, originId, targetId, type, amount, description, balanceAfter, createdAt);
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

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    // Helper methods for display
    public String getFormattedAmount() {
        return String.format("%.2f", amount.doubleValue());
    }

    public String getFormattedDate() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime today = now.toLocalDate().atStartOfDay();
        LocalDateTime yesterday = today.minusDays(1);

        if (createdAt.isAfter(today)) {
            return "Hoje, " + createdAt.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else if (createdAt.isAfter(yesterday)) {
            return "Ontem, " + createdAt.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else {
            return createdAt.format(DateTimeFormatter.ofPattern("dd/MM, HH:mm"));
        }
    }

    public String getDisplayName() {
        if (description != null && !description.trim().isEmpty()) {
            return description;
        }

        switch (type.toUpperCase()) {
            case "DEPOSIT":
                return "Depósito";
            case "WITHDRAW":
                return "Saque";
            case "TRANSFER":
                return "Transferência";
            default:
                return type;
        }
    }

    public boolean isIncoming(String accountId) {
        return accountId.equals(targetId) && !accountId.equals(originId);
    }

    public boolean isOutgoing(String accountId) {
        return accountId.equals(originId) && !accountId.equals(targetId);
    }

    public boolean isInternal(String accountId) {
        return accountId.equals(originId) && accountId.equals(targetId);
    }
}
