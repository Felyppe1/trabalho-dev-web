package com.trabalho.devweb.infrastructure.repositories;

import com.trabalho.devweb.application.interfaces.IInvestmentRepository;
import com.trabalho.devweb.domain.Investment;
import com.trabalho.devweb.domain.MyInvestment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class InvestmentsRepository implements IInvestmentRepository {
    private final Connection connection;

    public InvestmentsRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Investment> findAvailableInvestments() {
        List<Investment> investments = new ArrayList<>();
        String sql = "SELECT * FROM investment WHERE is_available = TRUE";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Investment investment = new Investment(
                        rs.getDate("expiration"),
                        rs.getString("category"),
                        rs.getBigDecimal("unit_price"),
                        rs.getBigDecimal("rentability_percent"),
                        rs.getString("rentability_index"),
                        rs.getBoolean("is_available"));
                investments.add(investment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return investments;
    }

    public List<MyInvestment> findInvestmentsByAccountId(String accountId) throws SQLException {
        String sql = """
                    WITH constants AS (
                        SELECT 0.04::numeric AS ipca, 0.13::numeric AS selic
                    ),
                    applications_with_yield AS (
                        SELECT
                            a.id,
                            a.account_id,
                            a.amount,
                            a.expiration,
                            a.category,
                            a.created_at,
                            i.rentability_percent,
                            i.rentability_index,
                            c.ipca,
                            c.selic,
                            DATE_PART('day', NOW() - a.created_at) / 365.0 AS years_elapsed,
                            a.amount * POWER(
                                1 + (
                                    CASE
                                        WHEN i.rentability_index = 'IPCA' THEN c.ipca
                                        WHEN i.rentability_index = 'SELIC' THEN c.selic
                                        ELSE 0
                                    END + i.rentability_percent / 100
                                ),
                                DATE_PART('day', NOW() - a.created_at) / 365.0
                            ) AS amount_with_yield
                        FROM application a
                        JOIN investment i ON a.expiration = i.expiration AND a.category = i.category
                        CROSS JOIN constants c
                        WHERE a.account_id = ?
                    )
                    SELECT
                        category,
                        expiration,
                        SUM(amount) AS total_invested,
                        SUM(amount_with_yield) AS total_with_yield,
                        SUM(amount_with_yield) - SUM(amount) AS total_profit,
                        ROUND(AVG(rentability_percent), 2) AS avg_rentability_percent,
                        MIN(rentability_index) AS rentability_index
                    FROM applications_with_yield
                    GROUP BY category, expiration
                    ORDER BY expiration;
                """;

        List<MyInvestment> results = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String category = rs.getString("category");
                    Date expiration = rs.getDate("expiration");
                    String investmentTitle = category + " " + expiration.toLocalDate().getYear();

                    MyInvestment myInvestment = new MyInvestment(
                            investmentTitle,
                            category,
                            expiration,
                            rs.getBigDecimal("total_invested"),
                            rs.getBigDecimal("total_with_yield"), // valor atual já calculado
                            BigDecimal.ZERO, // unit price não necessário para agregação
                            rs.getBigDecimal("avg_rentability_percent"),
                            rs.getString("rentability_index"),
                            new Timestamp(System.currentTimeMillis()));

                    results.add(myInvestment);
                }
            }
        }

        return results;
    }

    // @Override
    // public List<MyInvestment> findMyInvestmentsByAccountId(String accountId) {
    //     List<MyInvestment> myInvestments = new ArrayList<>();
    //     String sql = """
    //                 SELECT
    //                     i.category,
    //                     i.expiration,
    //                     i.unit_price,
    //                     i.rentability_percent,
    //                     i.rentability_index,
    //                     a.amount,
    //                     a.created_at
    //                 FROM application a
    //                 INNER JOIN investment i ON a.expiration = i.expiration AND a.category = i.category
    //                 WHERE a.account_id = ?
    //                 ORDER BY a.created_at DESC
    //             """;

    //     try (PreparedStatement stmt = connection.prepareStatement(sql)) {
    //         stmt.setString(1, accountId);
    //         try (ResultSet rs = stmt.executeQuery()) {
    //             while (rs.next()) {
    //                 String category = rs.getString("category");
    //                 Date expiration = rs.getDate("expiration");
    //                 String investmentTitle = category + " " + (expiration.toLocalDate().getYear());

    //                 MyInvestment myInvestment = new MyInvestment(
    //                         investmentTitle,
    //                         category,
    //                         expiration,
    //                         rs.getBigDecimal("amount"),
    //                         rs.getBigDecimal("unit_price"),
    //                         rs.getBigDecimal("rentability_percent"),
    //                         rs.getString("rentability_index"),
    //                         rs.getTimestamp("created_at"));
    //                 myInvestments.add(myInvestment);
    //             }
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return myInvestments;
    // }
}
