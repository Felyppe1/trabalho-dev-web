package com.trabalho.devweb.infrastructure.repositories;

import com.trabalho.devweb.application.interfaces.IAccountsRepository;
import com.trabalho.devweb.domain.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}