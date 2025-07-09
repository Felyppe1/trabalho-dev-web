package com.trabalho.devweb.infrastructure.repositories;

import com.trabalho.devweb.application.interfaces.ITransactionRepository;
import com.trabalho.devweb.domain.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionRepository implements ITransactionRepository {
    private Connection connection;

    public TransactionRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Transaction transaction) throws SQLException {
        String sql = """
                    INSERT INTO transaction (
                        id, origin_id, target_id, type, amount, description, balance_after, created_at
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, transaction.getId());
            stmt.setString(2, transaction.getOriginId());
            stmt.setString(3, transaction.getTargetId());
            stmt.setString(4, transaction.getType());
            stmt.setBigDecimal(5, transaction.getAmount());
            stmt.setString(6, transaction.getDescription());
            stmt.setBigDecimal(7, transaction.getBalanceAfter());
            stmt.setTimestamp(8, java.sql.Timestamp.valueOf(transaction.getCreatedAt()));

            stmt.executeUpdate();
        }
    }
}
