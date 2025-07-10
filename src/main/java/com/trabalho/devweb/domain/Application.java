package com.trabalho.devweb.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Application {
    private static final BigDecimal IPCA_RATE = new BigDecimal("0.04");
    private static final BigDecimal SELIC_RATE = new BigDecimal("0.13");

    private String id;
    private Date expiration;
    private String category;
    private String accountId;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private BigDecimal rentabilityPercent;
    private String rentabilityIndex;

    public Application(String id, Date expiration, String category, String accountId,
            BigDecimal amount, LocalDateTime createdAt) {
        this.id = id;
        this.expiration = expiration;
        this.category = category;
        this.accountId = accountId;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public Application(String id, Date expiration, String category, String accountId,
            BigDecimal amount, LocalDateTime createdAt, BigDecimal rentabilityPercent,
            String rentabilityIndex) {
        this.id = id;
        this.expiration = expiration;
        this.category = category;
        this.accountId = accountId;
        this.amount = amount;
        this.createdAt = createdAt;
        this.rentabilityPercent = rentabilityPercent;
        this.rentabilityIndex = rentabilityIndex;
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

    /**
     * Calcula o valor atual do investimento com rendimento
     * 
     * @return BigDecimal com o valor atual incluindo lucros
     */
    public BigDecimal calculateAmountWithYield() {
        if (amount == null || createdAt == null || rentabilityPercent == null) {
            return BigDecimal.ZERO;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime applicationDate = createdAt;

        double yearsElapsed = ChronoUnit.DAYS.between(applicationDate, now) / 365.0;

        // Calcular taxa total (rentabilidade + índice se aplicável)
        BigDecimal totalRate = rentabilityPercent.divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);

        if (rentabilityIndex != null) {
            switch (rentabilityIndex) {
                case "IPCA":
                    totalRate = totalRate.add(IPCA_RATE);
                    break;
                case "SELIC":
                    totalRate = totalRate.add(SELIC_RATE);
                    break;
            }
        }

        BigDecimal onePlusRate = BigDecimal.ONE.add(totalRate);
        double power = Math.pow(onePlusRate.doubleValue(), yearsElapsed);

        return amount.multiply(new BigDecimal(power)).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula o lucro total obtido
     * 
     * @return BigDecimal com o lucro (valor atual - valor investido)
     */
    public BigDecimal calculateProfit() {
        return calculateAmountWithYield().subtract(amount);
    }

    /**
     * Calcula a rentabilidade percentual atual
     * 
     * @return BigDecimal com a rentabilidade em percentual
     */
    public BigDecimal calculateCurrentProfitability() {
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal profit = calculateProfit();
        return profit.divide(amount, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
    }

    /**
     * Calcula o lucro proporcional a um valor específico
     * 
     * @param value valor para saber o lucro proporcional (deve ser menor ou igual ao
     *                      amount)
     * @return BigDecimal com o lucro proporcional ao valor especificado
     * @throws IllegalArgumentException se o valor for maior que o amount ou
     *                                  menor/igual a zero
     */
    public BigDecimal calculateProportionalProfit(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor a resgatar deve ser maior que zero");
        }

        if (value.compareTo(amount) > 0) {
            throw new IllegalArgumentException("Valor a resgatar não pode ser maior que o valor investido");
        }

        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalProfit = calculateProfit();

        BigDecimal proportion = value.divide(amount, 10, RoundingMode.HALF_UP);

        return totalProfit.multiply(proportion).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula o valor total de resgate (valor original + lucro proporcional)
     * 
     * @param valueToRedeem valor original a ser resgatado
     * @return BigDecimal com o valor total incluindo lucro proporcional
     */
    public BigDecimal calculateRedemptionValue(BigDecimal valueToRedeem) {
        BigDecimal proportionalProfit = calculateProportionalProfit(valueToRedeem);
        return valueToRedeem.add(proportionalProfit);
    }
}
