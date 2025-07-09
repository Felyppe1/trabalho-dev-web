package com.trabalho.devweb.infrastructure.repositories;

import com.trabalho.devweb.application.interfaces.IApplicationRepository;
import com.trabalho.devweb.domain.Application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
