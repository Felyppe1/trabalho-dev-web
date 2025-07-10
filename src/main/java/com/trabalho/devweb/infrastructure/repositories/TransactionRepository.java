package com.trabalho.devweb.infrastructure.repositories;

import com.trabalho.devweb.application.interfaces.ITransactionRepository;
import com.trabalho.devweb.domain.Transaction;
import com.trabalho.devweb.infrastructure.databaseconnection.PostgresConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Transaction> findByAccountIdAndMonth(String accountId, Integer month, Integer year) {
        List<Transaction> list = new ArrayList<>();

        String searchTrasaction = "SELECT * FROM transaction"+
                                    "WHERE (origin_id) = ? OR target_id = ?)"+
                                    "AND EXTRACT(MONTH FROM created_at) = ? " +
                                    "AND EXTRACT(YEAR FROM created_at) = ? " +
                                    "ORDER BY created_at DESC";
        try (Connection conn = PostgresConnection.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(searchTrasaction);
            statement.setString(1, accountId);
            statement.setString(2, accountId);
            statement.setInt(3, month);
            statement.setInt(4, year);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Transaction transaction = new Transaction(
                        result.getString("id"),
                        result.getString("origin_id"),
                        result.getString("target_id"),
                        result.getString("type"),
                        result.getBigDecimal("amount"),
                        result.getString("description"),
                        result.getBigDecimal("balance_after"),
                        result.getTimestamp("created_at").toLocalDateTime());
                list.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
