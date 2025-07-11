package com.trabalho.devweb.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String originId;
    private String targetId;
    private LocalDateTime createdAt;
    private String type;
    private BigDecimal amount;
    private String description;
    private BigDecimal balanceAfter;

    public Transaction() {}

    public Transaction(String originId, String targetId, LocalDateTime createdAt, 
                      String type, BigDecimal amount, String description, BigDecimal balanceAfter) {
        this.originId = originId;
        this.targetId = targetId;
        this.createdAt = createdAt;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.balanceAfter = balanceAfter;
    }

    // Getters and Setters
    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
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
