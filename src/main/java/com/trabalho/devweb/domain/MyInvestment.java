package com.trabalho.devweb.domain;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class MyInvestment {
    private String investmentTitle;
    private String category;
    private Date maturityDate;
    private BigDecimal amountInvested;
    private BigDecimal currentValue;
    private BigDecimal unitPrice;
    private BigDecimal rentabilityPercent;
    private String rentabilityIndex;
    private Timestamp createdAt;

    public MyInvestment(String investmentTitle, String category, Date maturityDate,
            BigDecimal amountInvested, BigDecimal currentValue, BigDecimal unitPrice,
            BigDecimal rentabilityPercent, String rentabilityIndex,
            Timestamp createdAt) {
        this.investmentTitle = investmentTitle;
        this.category = category;
        this.maturityDate = maturityDate;
        this.amountInvested = amountInvested;
        this.currentValue = currentValue;
        this.unitPrice = unitPrice;
        this.rentabilityPercent = rentabilityPercent;
        this.rentabilityIndex = rentabilityIndex;
        this.createdAt = createdAt;
    }

    public String getInvestmentTitle() {
        return investmentTitle;
    }

    public void setInvestmentTitle(String investmentTitle) {
        this.investmentTitle = investmentTitle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(Date maturityDate) {
        this.maturityDate = maturityDate;
    }

    public BigDecimal getAmountInvested() {
        return amountInvested;
    }

    public void setAmountInvested(BigDecimal amountInvested) {
        this.amountInvested = amountInvested;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getRentabilityPercent() {
        return rentabilityPercent;
    }

    public void setRentabilityPercent(BigDecimal rentabilityPercent) {
        this.rentabilityPercent = rentabilityPercent;
    }

    public String getRentabilityIndex() {
        return rentabilityIndex;
    }

    public void setRentabilityIndex(String rentabilityIndex) {
        this.rentabilityIndex = rentabilityIndex;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getFormattedReturn() {
        if (rentabilityIndex != null && !rentabilityIndex.isEmpty()) {
            return rentabilityIndex + " + " + rentabilityPercent + "%";
        }
        return rentabilityPercent + "%";
    }
}
