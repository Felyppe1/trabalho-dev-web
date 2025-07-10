package com.trabalho.devweb.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.trabalho.devweb.application.interfaces.IAccountsRepository;
import com.trabalho.devweb.application.interfaces.IApplicationRepository;
import com.trabalho.devweb.application.interfaces.IInvestmentRepository;
import com.trabalho.devweb.application.interfaces.ITransactionRepository;
import com.trabalho.devweb.domain.Account;
import com.trabalho.devweb.domain.Application;
import com.trabalho.devweb.domain.Investment;
import com.trabalho.devweb.domain.Transaction;

public class RedeemInvestmentService {

    private final Connection connection;
    private final IAccountsRepository accountRepository;
    private final IInvestmentRepository investmentRepository;
    private final ITransactionRepository transactionRepository;
    private final IApplicationRepository applicationRepository;

    // Constantes para índices de rentabilidade (podem ser configuráveis)
    private static final BigDecimal IPCA_RATE = new BigDecimal("0.04");
    private static final BigDecimal SELIC_RATE = new BigDecimal("0.13");

    public RedeemInvestmentService(Connection connection,
            IAccountsRepository accountRepository,
            IInvestmentRepository investmentRepository,
            ITransactionRepository transactionRepository,
            IApplicationRepository applicationRepository) {
        this.connection = connection;
        this.accountRepository = accountRepository;
        this.investmentRepository = investmentRepository;
        this.transactionRepository = transactionRepository;
        this.applicationRepository = applicationRepository;
    }

    public boolean execute(String accountId, String category, int year, BigDecimal amountToRedeem) throws SQLException {
        Account account = accountRepository.findById(accountId);
        if (account == null) {
            throw new RuntimeException("Conta não encontrada");
        }

        Investment investment = investmentRepository.findInvestmentByCategoryAndYear(category, year);
        if (investment == null) {
            throw new RuntimeException("Investimento não encontrado");
        }

        // Buscar aplicações do usuário para este investimento ordenadas por data (FIFO)
        List<Application> applications = applicationRepository.findByAccountIdAndCategoryAndYear(accountId, category,
                year);

        if (applications.isEmpty()) {
            throw new RuntimeException("Você não possui investimentos nesse título");
        }

        try {
            connection.setAutoCommit(false);

            BigDecimal totalRedeemed = BigDecimal.ZERO;
            BigDecimal remainingToRedeem = amountToRedeem;

            for (Application application : applications) {
                if (remainingToRedeem.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }

                // Calcular valor atual da aplicação com rendimento
                BigDecimal currentValue = calculateCurrentValue(application, investment);

                if (remainingToRedeem.compareTo(currentValue) >= 0) {
                    // Resgatar toda a aplicação
                    totalRedeemed = totalRedeemed.add(currentValue);
                    remainingToRedeem = remainingToRedeem.subtract(currentValue);

                    // Deletar a aplicação
                    applicationRepository.delete(application.getId());
                } else {
                    // Resgatar parcialmente
                    BigDecimal proportionalProfit = calculateProportionalProfit(
                            application, investment, remainingToRedeem, currentValue);

                    totalRedeemed = totalRedeemed.add(remainingToRedeem);

                    // Calcular novo valor da aplicação (valor original - proporcional resgatado)
                    BigDecimal proportionalOriginal = application.getAmount()
                            .multiply(remainingToRedeem)
                            .divide(currentValue, 10, RoundingMode.HALF_UP);

                    BigDecimal newAmount = application.getAmount().subtract(proportionalOriginal);

                    // Atualizar a aplicação com o novo valor
                    applicationRepository.updateAmount(application.getId(), newAmount);

                    remainingToRedeem = BigDecimal.ZERO;
                }
            }

            if (totalRedeemed.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Não foi possível resgatar o valor solicitado");
            }

            // Creditar valor na conta
            account.credit(totalRedeemed);
            accountRepository.updateAccount(account);

            // Criar transação de resgate
            String description = String.format("Resgate de %s %d - Valor: R$ %.2f",
                    category, year, totalRedeemed);
            Transaction transaction = Transaction.createRedemption(
                    account.getId(), totalRedeemed, description, account.getBalance());
            transactionRepository.save(transaction);

            connection.commit();
            return true;

        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private BigDecimal calculateCurrentValue(Application application, Investment investment) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime applicationDate = application.getCreatedAt();

        // Calcular anos decorridos
        double yearsElapsed = ChronoUnit.DAYS.between(applicationDate, now) / 365.0;

        // Calcular taxa total (rentabilidade + índice se aplicável)
        BigDecimal totalRate = investment.getRentabilityPercent().divide(new BigDecimal("100"));

        if (investment.getRentabilityIndex() != null) {
            switch (investment.getRentabilityIndex()) {
                case "IPCA":
                    totalRate = totalRate.add(IPCA_RATE);
                    break;
                case "SELIC":
                    totalRate = totalRate.add(SELIC_RATE);
                    break;
            }
        }

        // Calcular valor com juros compostos: valor * (1 + taxa)^anos
        BigDecimal onePlusRate = BigDecimal.ONE.add(totalRate);
        double power = Math.pow(onePlusRate.doubleValue(), yearsElapsed);

        return application.getAmount().multiply(new BigDecimal(power)).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateProportionalProfit(Application application, Investment investment,
            BigDecimal amountToRedeem, BigDecimal currentValue) {

        BigDecimal originalAmount = application.getAmount();
        BigDecimal totalProfit = currentValue.subtract(originalAmount);

        // Calcular lucro proporcional ao valor sendo resgatado
        BigDecimal proportionalProfit = totalProfit
                .multiply(amountToRedeem)
                .divide(currentValue, 10, RoundingMode.HALF_UP);

        return proportionalProfit;
    }
}
