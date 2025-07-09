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

    @Override
    public List<MyInvestment> findMyInvestmentsByAccountId(String accountId) {
        List<MyInvestment> myInvestments = new ArrayList<>();
        String sql = """
                    SELECT
                        i.category,
                        i.expiration,
                        i.unit_price,
                        i.rentability_percent,
                        i.rentability_index,
                        a.amount,
                        a.created_at
                    FROM application a
                    INNER JOIN investment i ON a.expiration = i.expiration AND a.category = i.category
                    WHERE a.account_id = ?
                    ORDER BY a.created_at DESC
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String category = rs.getString("category");
                    Date expiration = rs.getDate("expiration");
                    String investmentTitle = category + " " + (expiration.toLocalDate().getYear());

                    MyInvestment myInvestment = new MyInvestment(
                            investmentTitle,
                            category,
                            expiration,
                            rs.getBigDecimal("amount"),
                            rs.getBigDecimal("unit_price"),
                            rs.getBigDecimal("rentability_percent"),
                            rs.getString("rentability_index"),
                            rs.getTimestamp("created_at"));
                    myInvestments.add(myInvestment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return myInvestments;
    }
}
