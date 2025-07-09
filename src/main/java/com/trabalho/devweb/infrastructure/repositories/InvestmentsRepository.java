package com.trabalho.devweb.infrastructure.repositories;

import com.trabalho.devweb.application.interfaces.IInvestmentRepository;
import com.trabalho.devweb.domain.Investment;
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
}
