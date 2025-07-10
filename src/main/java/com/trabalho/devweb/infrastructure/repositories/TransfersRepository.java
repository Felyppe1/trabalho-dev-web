package com.trabalho.devweb.infrastructure.repositories;

import com.trabalho.devweb.application.interfaces.ITransfersRepository;
import com.trabalho.devweb.domain.Transfer;

import java.sql.*;
import java.math.BigDecimal;
import java.util.*;

public class TransfersRepository implements ITransfersRepository {
    private final Connection connection;

    public TransfersRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Transfer> findTransfersByAccount(String accountId, int offset, int limit, String direction, String nameFilter) throws SQLException {
    String baseQuery = """
        SELECT t.origin_id, t.target_id, t.created_at, t.type, t.amount, t.description, t.balance_after,
               a.name AS target_name, a.number, a.digit
        FROM transaction t
        JOIN account a ON (CASE WHEN t.origin_id = ? THEN t.target_id ELSE t.origin_id END) = a.id
        WHERE (? = t.origin_id OR ? = t.target_id)
          AND t.origin_id IS NOT NULL
          AND t.target_id IS NOT NULL
    """;

    if ("sent".equalsIgnoreCase(direction)) {
        baseQuery += " AND t.origin_id = ? ";
    } else if ("received".equalsIgnoreCase(direction)) {
        baseQuery += " AND t.target_id = ? ";
    }

    if (nameFilter != null && !nameFilter.trim().isEmpty()) {
        baseQuery += " AND LOWER(a.name) LIKE ? ";
    }

    baseQuery += " ORDER BY t.created_at DESC OFFSET ? LIMIT ?";

    PreparedStatement stmt = connection.prepareStatement(baseQuery);

    int i = 1;
    stmt.setString(i++, accountId);
    stmt.setString(i++, accountId);
    stmt.setString(i++, accountId);

    if ("sent".equalsIgnoreCase(direction) || "received".equalsIgnoreCase(direction)) {
        stmt.setString(i++, accountId);
    }

    if (nameFilter != null && !nameFilter.trim().isEmpty()) {
        stmt.setString(i++, "%" + nameFilter.toLowerCase() + "%");
    }

    stmt.setInt(i++, offset);
    stmt.setInt(i, limit);

    List<Transfer> transfers = new ArrayList<>();
    try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            Transfer t = new Transfer();
            t.setOriginId(rs.getString("origin_id"));
            t.setTargetId(rs.getString("target_id"));
            t.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            t.setType(rs.getString("type"));
            t.setAmount(rs.getBigDecimal("amount"));
            t.setDescription(rs.getString("description"));
            t.setBalanceAfter(rs.getBigDecimal("balance_after"));
            t.setTargetName(rs.getString("target_name"));
            t.setTargetAccountNumber(rs.getString("number") + "-" + rs.getString("digit"));
            t.setIsReceived(!accountId.equals(rs.getString("origin_id")));
            transfers.add(t);
        }
    }

    return transfers;
}

 
    @Override
    public void save(Transfer transfer) throws SQLException {
    String sql = """
        INSERT INTO transaction (
            id, origin_id, target_id, type, amount, description, balance_after, created_at
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        // Gerar um UUID para o id da transação
        String id = java.util.UUID.randomUUID().toString();

        stmt.setString(1, id);
        stmt.setString(2, transfer.getOriginId());
        stmt.setString(3, transfer.getTargetId());
        stmt.setString(4, transfer.getType());  // por exemplo, "transfer"
        stmt.setBigDecimal(5, transfer.getAmount());
        stmt.setString(6, transfer.getDescription());
        stmt.setBigDecimal(7, transfer.getBalanceAfter());
        stmt.setTimestamp(8, java.sql.Timestamp.valueOf(transfer.getCreatedAt()));

        stmt.executeUpdate();
    }
}



    @Override
    public int countTransfersByAccount(String accountId, String direction, String nameFilter) throws SQLException {
        String sql = """
            SELECT COUNT(*)
            FROM transaction t
            JOIN account a ON (CASE WHEN t.origin_id = ? THEN t.target_id ELSE t.origin_id END) = a.id
            WHERE (? = t.origin_id OR ? = t.target_id)
        """;

        if ("sent".equalsIgnoreCase(direction)) {
            sql += " AND t.origin_id = ? ";
        } else if ("received".equalsIgnoreCase(direction)) {
            sql += " AND t.target_id = ? ";
        }

        if (nameFilter != null && !nameFilter.trim().isEmpty()) {
            sql += " AND LOWER(a.name) LIKE ? ";
        }

        PreparedStatement stmt = connection.prepareStatement(sql);

        int i = 1;
        stmt.setString(i++, accountId);
        stmt.setString(i++, accountId);
        stmt.setString(i++, accountId);

        if ("sent".equalsIgnoreCase(direction) || "received".equalsIgnoreCase(direction)) {
            stmt.setString(i++, accountId);
        }

        if (nameFilter != null && !nameFilter.trim().isEmpty()) {
            stmt.setString(i++, "%" + nameFilter.toLowerCase() + "%");
        }

        ResultSet rs = stmt.executeQuery();
        return rs.next() ? rs.getInt(1) : 0;
    }

    @Override
    public List<Transfer> findTopFrequentRecipients(String accountId, int limit) throws SQLException {
        String sql = """
            SELECT a.name, a.number, a.digit, COUNT(*) as total
            FROM transaction t
            JOIN account a ON a.id = t.target_id
            WHERE t.origin_id = ?
            GROUP BY a.name, a.number, a.digit
            ORDER BY total DESC
            LIMIT ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountId);
            stmt.setInt(2, limit);

            List<Transfer> frequents = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transfer t = new Transfer();
                    t.setTargetName(rs.getString("name"));
                    t.setTargetAccountNumber(rs.getString("number") + "-" + rs.getString("digit"));
                    frequents.add(t);
                }
            }
            return frequents;
        }
    }

    @Override
    public BigDecimal getTotalSent(String accountId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM transaction WHERE origin_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
            }
        }
    }

    @Override
    public BigDecimal getTotalReceived(String accountId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM transaction WHERE target_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
            }
        }
    }
}
