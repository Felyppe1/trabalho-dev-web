package com.trabalho.devweb.infrastructure.repositories;

import com.trabalho.devweb.application.interfaces.IApplicationRepository;
import com.trabalho.devweb.domain.Application;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationRepository implements IApplicationRepository {
    private Connection connection;

    public ApplicationRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Application application) throws SQLException {
        String sql = """
                    INSERT INTO application (
                        id, expiration, category, account_id, amount, created_at
                    ) VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, application.getId());
            stmt.setDate(2, application.getExpiration());
            stmt.setString(3, application.getCategory());
            stmt.setString(4, application.getAccountId());
            stmt.setBigDecimal(5, application.getAmount());
            stmt.setTimestamp(6, java.sql.Timestamp.valueOf(application.getCreatedAt()));

            stmt.executeUpdate();
        }
    }

    @Override
    public List<Application> findByAccountIdAndCategoryAndYear(String accountId, String category, int year)
            throws SQLException {
        String sql = """
                    SELECT a.id, a.expiration, a.category, a.account_id, a.amount, a.created_at
                    FROM application a
                    JOIN investment i ON a.expiration = i.expiration AND a.category = i.category
                    WHERE a.account_id = ? AND a.category = ? AND EXTRACT(YEAR FROM i.expiration) = ?
                    ORDER BY a.created_at ASC
                """;

        List<Application> applications = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountId);
            stmt.setString(2, category);
            stmt.setInt(3, year);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Application application = new Application(
                            rs.getString("id"),
                            rs.getDate("expiration"),
                            rs.getString("category"),
                            rs.getString("account_id"),
                            rs.getBigDecimal("amount"),
                            rs.getTimestamp("created_at").toLocalDateTime());
                    applications.add(application);
                }
            }
        }

        return applications;
    }

    @Override
    public void delete(String applicationId) throws SQLException {
        String sql = "DELETE FROM application WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, applicationId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateAmount(String applicationId, BigDecimal newAmount) throws SQLException {
        String sql = "UPDATE application SET amount = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, newAmount);
            stmt.setString(2, applicationId);
            stmt.executeUpdate();
        }
    }
}
