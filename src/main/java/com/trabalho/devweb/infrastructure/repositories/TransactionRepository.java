package com.trabalho.devweb.infrastructure.repositories;

import com.trabalho.devweb.domain.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {
    private final Connection connection;

    public TransactionRepository(Connection connection) {
        this.connection = connection;
    }

    public List<Transaction> findRecentByAccountId(String accountId, int limit) throws SQLException {
        String sql = "SELECT origin_id, target_id, created_at, type, amount, description, balance_after " +
                    "FROM transaction " +
                    "WHERE origin_id = ? OR target_id = ? " +
                    "ORDER BY created_at DESC " +
                    "LIMIT ?";

        List<Transaction> transactions = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountId);
            stmt.setString(2, accountId);
            stmt.setInt(3, limit);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setOriginId(rs.getString("origin_id"));
                transaction.setTargetId(rs.getString("target_id"));
                transaction.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                transaction.setType(rs.getString("type"));
                transaction.setAmount(rs.getBigDecimal("amount"));
                transaction.setDescription(rs.getString("description"));
                transaction.setBalanceAfter(rs.getBigDecimal("balance_after"));

                transactions.add(transaction);
            }
        }

        return transactions;
    }

    public List<Transaction> findAllByAccountId(String accountId) throws SQLException {
        String sql = "SELECT origin_id, target_id, created_at, type, amount, description, balance_after " +
                    "FROM transaction " +
                    "WHERE origin_id = ? OR target_id = ? " +
                    "ORDER BY created_at DESC";

        List<Transaction> transactions = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountId);
            stmt.setString(2, accountId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setOriginId(rs.getString("origin_id"));
                transaction.setTargetId(rs.getString("target_id"));
                transaction.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                transaction.setType(rs.getString("type"));
                transaction.setAmount(rs.getBigDecimal("amount"));
                transaction.setDescription(rs.getString("description"));
                transaction.setBalanceAfter(rs.getBigDecimal("balance_after"));

                transactions.add(transaction);
            }
        }

        return transactions;
    }
}
