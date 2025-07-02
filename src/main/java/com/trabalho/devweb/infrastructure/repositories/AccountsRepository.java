package com.trabalho.devweb.infrastructure.repositories;

import com.trabalho.devweb.application.interfaces.IAccountsRepository;
import com.trabalho.devweb.domain.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class AccountsRepository implements IAccountsRepository {
    private Connection connection;

    public AccountsRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Account account) throws SQLException {
        String sql = """
                    INSERT INTO account (
                        id, number, digit, cpf, name, email, password, birth_date, address, cellphone_number, balance, status, created_at
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, account.getId());
            stmt.setString(2, account.getNumber());
            stmt.setString(3, account.getDigit());
            stmt.setString(4, account.getCpf());
            stmt.setString(5, account.getName());
            stmt.setString(6, account.getEmail());
            stmt.setString(7, account.getPassword());
            stmt.setDate(8, java.sql.Date.valueOf(account.getBirthDate())); // LocalDate -> SQL Date
            stmt.setString(9, account.getAddress());
            stmt.setString(10, account.getCellphoneNumber());
            stmt.setBigDecimal(11, account.getBalance());
            stmt.setString(12, account.getStatus());
            stmt.setTimestamp(13, java.sql.Timestamp.valueOf(account.getCreatedAt())); // LocalDateTime -> SQL Timestamp

            stmt.executeUpdate();
        }
    }

    @Override
    public boolean checkIfAlreadyExists(String cpf, String email, String cellphoneNumber) throws SQLException {
        String sql = """
                    SELECT
                        cpf,
                        email,
                        cellphone_number
                    FROM account
                    WHERE cpf = ?
                    OR email = ?
                    OR cellphone_number = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setString(2, email);
            stmt.setString(3, cellphoneNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public Account findById(String id) throws SQLException {
        String sql = """
                    SELECT id, number, digit, cpf, name, email, password, birth_date, address, cellphone_number, balance, status, created_at
                    FROM account
                    WHERE id = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                }
                return null;
            }
        }
    }

    @Override
    public Account findOneByCpf(String cpf) throws SQLException {
        String sql = """
                    SELECT id, number, digit, cpf, name, email, password, birth_date, address, cellphone_number, balance, status, created_at
                    FROM account
                    WHERE cpf = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                }
                return null;
            }
        }
    }

    @Override
    public Account findOneByEmail(String email) throws SQLException {
        String sql = """
                    SELECT id, number, digit, cpf, name, email, password, birth_date, address, cellphone_number, balance, status, created_at
                    FROM account
                    WHERE email = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                }
                return null;
            }
        }
    }

    @Override
    public Account findOneByCellphoneNumber(String cellphoneNumber) throws SQLException {
        String sql = """
                    SELECT id, number, digit, cpf, name, email, password, birth_date, address, cellphone_number, balance, status, created_at
                    FROM account
                    WHERE cellphone_number = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cellphoneNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                }
                return null;
            }
        }
    }

    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setId(rs.getString("id"));
        account.setNumber(rs.getString("number"));
        account.setDigit(rs.getString("digit"));
        account.setCpf(rs.getString("cpf"));
        account.setName(rs.getString("name"));
        account.setEmail(rs.getString("email"));
        account.setPassword(rs.getString("password"));
        account.setBirthDate(rs.getDate("birth_date").toLocalDate());
        account.setAddress(rs.getString("address"));
        account.setCellphoneNumber(rs.getString("cellphone_number"));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setStatus(rs.getString("status"));
        account.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return account;
    }

}
