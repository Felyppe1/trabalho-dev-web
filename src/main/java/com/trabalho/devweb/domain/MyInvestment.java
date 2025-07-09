package com.trabalho.devweb.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class MyInvestment {
    // Constantes para os índices de referência (valores anuais aproximados)
    private static final BigDecimal IPCA_RATE = new BigDecimal("4.50"); // 4.5% ao ano
    private static final BigDecimal SELIC_RATE = new BigDecimal("11.75"); // 11.75% ao ano
    private String investmentTitle;
    private String category;
    private Date maturityDate;
    private BigDecimal amountInvested;
    private BigDecimal unitPrice;
    private BigDecimal rentabilityPercent;
    private String rentabilityIndex;
    private Timestamp createdAt;

    public MyInvestment(String investmentTitle, String category, Date maturityDate,
            BigDecimal amountInvested, BigDecimal unitPrice,
            BigDecimal rentabilityPercent, String rentabilityIndex,
            Timestamp createdAt) {
        this.investmentTitle = investmentTitle;
        this.category = category;
        this.maturityDate = maturityDate;
        this.amountInvested = amountInvested;
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

    public BigDecimal getCurrentValue() {
        if (amountInvested == null || rentabilityPercent == null) {
            return amountInvested != null ? amountInvested : BigDecimal.ZERO;
        }

        // Calcular o tempo decorrido em anos
        LocalDate createdDate = createdAt.toLocalDateTime().toLocalDate();
        LocalDate maturityLocalDate = maturityDate.toLocalDate();
        long daysBetween = ChronoUnit.DAYS.between(createdDate, maturityLocalDate);
        BigDecimal yearsToMaturity = new BigDecimal(daysBetween).divide(new BigDecimal("365"), 4, RoundingMode.HALF_UP);

        // Determinar o índice base baseado na categoria
        BigDecimal baseIndex = BigDecimal.ZERO;
        if (rentabilityIndex != null) {
            switch (rentabilityIndex.toUpperCase()) {
                case "IPCA":
                    baseIndex = IPCA_RATE;
                    break;
                case "SELIC":
                    baseIndex = SELIC_RATE;
                    break;
                default:
                    baseIndex = BigDecimal.ZERO;
                    break;
            }
        }

        // Calcular a taxa total: (baseIndex * rentabilityPercent) / 100
        // Se não há índice, usa apenas rentabilityPercent
        BigDecimal totalRate;
        if (baseIndex.compareTo(BigDecimal.ZERO) > 0) {
            totalRate = baseIndex.multiply(rentabilityPercent).divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP);
        } else {
            totalRate = rentabilityPercent.divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP);
        }

        // Aplicar a fórmula: amount * (1 + rate) ^ years
        BigDecimal onePlusRate = BigDecimal.ONE.add(totalRate);

        // Usar Math.pow para calcular a potência e converter de volta para BigDecimal
        double result = amountInvested.doubleValue()
                * Math.pow(onePlusRate.doubleValue(), yearsToMaturity.doubleValue());

        return new BigDecimal(result).setScale(2, RoundingMode.HALF_UP);
    }

    public String getFormattedReturn() {
        if (rentabilityIndex != null && !rentabilityIndex.isEmpty()) {
            return rentabilityIndex + " + " + rentabilityPercent + "%";
        }
        return rentabilityPercent + "%";
    }

    // Método auxiliar para obter a taxa base do índice
    public BigDecimal getBaseIndexRate() {
        if (rentabilityIndex != null) {
            switch (rentabilityIndex.toUpperCase()) {
                case "IPCA":
                    return IPCA_RATE;
                case "SELIC":
                    return SELIC_RATE;
                default:
                    return BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }
}
