package com.trabalho.devweb.application;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

import com.trabalho.devweb.domain.Account;
import com.trabalho.devweb.domain.Investment;

public class BuyInvestmentService {

    private final Connection connection;

    public BuyInvestmentService(Connection connection) {
        this.connection = connection;
    }

    public boolean execute(Account account, String investmentType, BigDecimal amount) throws SQLException {
    //     try {
    //         connection.setAutoCommit(false);

    //         // 1. Verificar se o investimento existe e está disponível
    //         Investment investment = getInvestmentByType(investmentType);
    //         if (investment == null) {
    //             throw new RuntimeException("Investment not found");
    //         }

    //         // 2. Verificar valor mínimo
    //         if (amount.compareTo(investment.getMinimumInvestment()) < 0) {
    //             throw new RuntimeException("Amount below minimum investment");
    //         }

    //         // 3. Verificar saldo da conta
    //         if (account.getBalance().compareTo(amount) < 0) {
    //             throw new RuntimeException("Insufficient balance");
    //         }

    //         // 4. Debitar da conta
    //         updateAccountBalance(account.getId(), account.getBalance().subtract(amount));

    //         // 5. Criar registro do investimento
    //         createMyInvestment(account.getId(), investment.getId(), amount);

    //         connection.commit();
    //         return true;

    //     } catch (Exception e) {
    //         connection.rollback();
    //         throw e;
    //     } finally {
    //         connection.setAutoCommit(true);
    //     }
    // }

    // private Investment getInvestmentByType(String investmentType) throws SQLException {
    //     String sql = "SELECT * FROM investments WHERE name = ? LIMIT 1";
    //     try (PreparedStatement stmt = connection.prepareStatement(sql)) {
    //         stmt.setString(1, investmentType);
    //         var rs = stmt.executeQuery();

    //         if (rs.next()) {
    //             Investment investment = new Investment();
    //             investment.setId(rs.getString("id"));
    //             investment.setName(rs.getString("name"));
    //             investment.setDescription(rs.getString("description"));
    //             investment.setAnnualReturn(rs.getBigDecimal("annual_return"));
    //             investment.setMinimumInvestment(rs.getBigDecimal("minimum_investment"));
    //             investment.setUnitPrice(rs.getBigDecimal("unit_price"));
    //             investment.setMaturityDate(rs.getDate("maturity_date"));
    //             return investment;
    //         }
    //     }
        return true;
    }

    // private void updateAccountBalance(String accountId, BigDecimal newBalance) throws SQLException {
    //     String sql = "UPDATE accounts SET balance = ? WHERE id = ?";
    //     try (PreparedStatement stmt = connection.prepareStatement(sql)) {
    //         stmt.setBigDecimal(1, newBalance);
    //         stmt.setString(2, accountId);
    //         stmt.executeUpdate();
    //     }
    // }

    // private void createMyInvestment(String accountId, String investmentId, BigDecimal amount) throws SQLException {
    //     String sql = "INSERT INTO my_investments (id, account_id, investment_id, amount, purchase_date) VALUES (?, ?, ?, ?, ?)";
    //     try (PreparedStatement stmt = connection.prepareStatement(sql)) {
    //         stmt.setString(1, UUID.randomUUID().toString());
    //         stmt.setString(2, accountId);
    //         stmt.setString(3, investmentId);
    //         stmt.setBigDecimal(4, amount);
    //         stmt.setTimestamp(5, java.sql.Timestamp.valueOf(LocalDateTime.now()));
    //         stmt.executeUpdate();
    //     }
    // }
}
