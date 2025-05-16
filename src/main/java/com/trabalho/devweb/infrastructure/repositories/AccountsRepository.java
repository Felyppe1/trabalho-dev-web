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
                cpf, name, email, password, birth_date, address, cellphone_number, balance, status
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, account.getCpf());
            stmt.setString(2, account.getName());
            stmt.setString(3, account.getEmail());
            stmt.setString(4, account.getPassword());
            stmt.setDate(5, java.sql.Date.valueOf(account.getBirthDate())); // LocalDate -> SQL Date
            stmt.setString(6, account.getAddress());
            stmt.setString(7, account.getCellphoneNumber());
            stmt.setBigDecimal(8, account.getBalance());
            stmt.setString(9, account.getStatus());
            
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
    public Account findOneByCpf(String cpf) throws SQLException {
        String sql = """
            SELECT cpf, name, email, password, birth_date, address, cellphone_number
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
            SELECT cpf, name, email, password, birth_date, address, cellphone_number
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
            SELECT cpf, name, email, password, birth_date, address, cellphone_number
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
        return new Account(
            rs.getString("cpf"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getDate("birth_date").toLocalDate(),
            rs.getString("address"),
            rs.getString("cellphone_number")
        );
    }

}
