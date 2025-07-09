package com.trabalho.devweb.domain;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

public class Application {
    private String id;
    private Date expiration;
    private String category;
    private String accountId;
    private BigDecimal amount;
    private LocalDateTime createdAt;

    public Application(String id, Date expiration, String category, String accountId,
            BigDecimal amount, LocalDateTime createdAt) {
        this.id = id;
        this.expiration = expiration;
        this.category = category;
        this.accountId = accountId;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public static Application create(Date expiration, String category, String accountId, BigDecimal amount) {
        String id = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();
        return new Application(id, expiration, category, accountId, amount, createdAt);
    }

    public String getId() {
        return id;
    }

    public Date getExpiration() {
        return expiration;
    }

    public String getCategory() {
        return category;
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
