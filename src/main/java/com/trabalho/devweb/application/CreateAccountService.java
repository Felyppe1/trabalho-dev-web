package com.trabalho.devweb.application;

import com.trabalho.devweb.domain.Account;

import com.trabalho.devweb.application.interfaces.IAccountsRepository;

import java.time.LocalDate;

import java.sql.SQLException;

public class CreateAccountService {
    private IAccountsRepository accountsRepository;

    public CreateAccountService(IAccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public void execute(String cpf, String name, String email, String password, LocalDate birthDate, String address, String cellphoneNumber) throws SQLException {
        boolean accountExists = this.accountsRepository.checkIfAlreadyExists(cpf, email, cellphoneNumber);

        if (accountExists) {
            throw new SQLException("Conta já existe com esse CPF, email ou número de celular.");
        }

        Account account = new Account(
            cpf,
            name,
            email,
            password,
            birthDate,
            address,
            cellphoneNumber
        );

        this.accountsRepository.save(account);
    }
}
