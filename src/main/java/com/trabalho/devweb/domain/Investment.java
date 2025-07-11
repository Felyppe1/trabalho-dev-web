package com.trabalho.devweb.domain;

import java.math.BigDecimal;
import java.sql.Date;

public class Investment {
    private Date expiration;
    private String category;
    private BigDecimal unitPrice;
    private BigDecimal rentabilityPercent;
    private String rentabilityIndex;
    private boolean isAvailable;

    public Investment(Date expiration, String category, BigDecimal unitPrice, BigDecimal rentabilityPercent,
            String rentabilityIndex, boolean isAvailable) {
        this.expiration = expiration;
        this.category = category;
        this.unitPrice = unitPrice;
        this.rentabilityPercent = rentabilityPercent;
        this.rentabilityIndex = rentabilityIndex;
        this.isAvailable = isAvailable;
    }

    public Date getExpiration() {
        return expiration;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getRentabilityPercent() {
        return rentabilityPercent;
    }

    public String getRentabilityIndex() {
        return rentabilityIndex;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setRentabilityPercent(BigDecimal rentabilityPercent) {
        this.rentabilityPercent = rentabilityPercent;
    }

    public void setRentabilityIndex(String rentabilityIndex) {
        this.rentabilityIndex = rentabilityIndex;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
