package com.trabalho.devweb.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.trabalho.devweb.application.interfaces.IAccountsRepository;
import com.trabalho.devweb.application.interfaces.IApplicationRepository;
import com.trabalho.devweb.application.interfaces.IInvestmentRepository;
import com.trabalho.devweb.application.interfaces.ITransactionRepository;
import com.trabalho.devweb.domain.Account;
import com.trabalho.devweb.domain.Investment;
import com.trabalho.devweb.domain.Application;
import com.trabalho.devweb.domain.Transaction;

public class RedeemInvestmentService {

    private final Connection connection;
    private final IAccountsRepository accountRepository;
    private final IInvestmentRepository investmentRepository;
    private final ITransactionRepository transactionRepository;
    private final IApplicationRepository applicationRepository;

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

    public BigDecimal execute(String accountId, String category, int year, BigDecimal amountToRedeem) throws SQLException {
        Account account = accountRepository.findById(accountId);
        if (account == null) {
            throw new RuntimeException("Conta não encontrada");
        }

        Investment investment = investmentRepository.findInvestmentByCategoryAndYear(category, year);
        if (investment == null) {
            throw new RuntimeException("T não encontrado");
        }

        List<Application> applications = applicationRepository.findApplicationsByAccountIdAndCategoryAndYear(accountId,
                category, year, "DESC");

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

                BigDecimal currentValue = application.calculateAmountWithYield();
                BigDecimal originalAmount = application.getAmount();

                if (remainingToRedeem.compareTo(currentValue) >= 0) {
                    totalRedeemed = totalRedeemed.add(currentValue);
                    remainingToRedeem = remainingToRedeem.subtract(currentValue);

                    applicationRepository.delete(application.getId());
                } else {
                    totalRedeemed = totalRedeemed.add(remainingToRedeem);

                    totalRedeemed = totalRedeemed.add(application.calculateProportionalProfit(remainingToRedeem));

                    BigDecimal newAmount = originalAmount.subtract(remainingToRedeem);

                    applicationRepository.updateAmount(application.getId(), newAmount);

                    remainingToRedeem = BigDecimal.ZERO;
                }
            }

            if (remainingToRedeem.compareTo(BigDecimal.ZERO) > 0) {
                throw new RuntimeException("Não foi possível resgatar o valor solicitado");
            }

            account.credit(totalRedeemed);
            accountRepository.updateAccount(account);

            String description = String.format("Resgate de %s %d - Valor: R$ %.2f",
                    category, year, totalRedeemed);
            Transaction transaction = Transaction.createRedemption(
                    account.getId(), totalRedeemed, description, account.getBalance());
            transactionRepository.save(transaction);

            connection.commit();
            return totalRedeemed;

        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
