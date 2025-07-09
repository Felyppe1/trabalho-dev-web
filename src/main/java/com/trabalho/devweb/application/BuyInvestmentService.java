package com.trabalho.devweb.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import com.trabalho.devweb.application.interfaces.IAccountsRepository;
import com.trabalho.devweb.application.interfaces.IApplicationRepository;
import com.trabalho.devweb.application.interfaces.IInvestmentRepository;
import com.trabalho.devweb.application.interfaces.ITransactionRepository;
import com.trabalho.devweb.domain.Account;
import com.trabalho.devweb.domain.Application;
import com.trabalho.devweb.domain.Investment;
import com.trabalho.devweb.domain.Transaction;

public class BuyInvestmentService {

    private final Connection connection;
    private final IAccountsRepository accountRepository;
    private final IInvestmentRepository investmentRepository;
    private final ITransactionRepository transactionRepository;
    private final IApplicationRepository applicationRepository;

    public BuyInvestmentService(Connection connection,
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

    public boolean execute(String accountId, String category, int year, BigDecimal amount) throws SQLException {
        Account account = accountRepository.findById(accountId);
        if (account == null) {
            throw new RuntimeException("Conta não encontrada");
        }

        Investment investment = investmentRepository.findInvestmentByCategoryAndYear(category, year);
        if (investment == null) {
            throw new RuntimeException("Investimento não encontrado");
        }

        try {
            connection.setAutoCommit(false);

            BigDecimal minimumInvestment = investment.getUnitPrice().divide(new BigDecimal("100"));
            if (amount.compareTo(minimumInvestment) < 0) {
                minimumInvestment = minimumInvestment.setScale(2, RoundingMode.HALF_UP);
                String formattedValue = minimumInvestment.toString().replace('.', ',');
                throw new RuntimeException("Valor abaixo do investimento mínimo de R$ " +
                        formattedValue);
            }

            // if (account.getBalance().compareTo(amount) < 0) {
            //     throw new RuntimeException("Saldo insuficiente");
            // }

            account.debit(amount);

            accountRepository.updateAccount(account);

            String description = "Investimento em " + category + " " + year;
            Transaction transaction = Transaction.create(
                    account.getId(), amount, description, account.getBalance());
            transactionRepository.save(transaction);

            Date expiration = Date.valueOf(investment.getExpiration().toLocalDate());
            Application application = Application.create(expiration, category, account.getId(), amount);
            applicationRepository.save(application);

            connection.commit();
            return true;

        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
