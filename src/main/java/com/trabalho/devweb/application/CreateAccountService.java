package com.trabalho.devweb.application;

import com.trabalho.devweb.domain.Account;

import com.trabalho.devweb.application.errors.BusinessRuleException;
import com.trabalho.devweb.application.errors.ValidationException;
import com.trabalho.devweb.application.interfaces.IAccountsRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.SQLException;

public class CreateAccountService {
    private IAccountsRepository accountsRepository;

    public CreateAccountService(IAccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public void execute(String cpf, String name, String email, String password, LocalDate birthDate, String address, String cellphoneNumber) throws BusinessRuleException, SQLException {
        List<Map<String, String>> errors = new ArrayList<>();

        try {
            if (accountsRepository.findOneByCpf(cpf) != null) {
                errors.add(Map.of("cpf", "Conta já existe com esse CPF."));
            }

            if (accountsRepository.findOneByEmail(email) != null) {
                errors.add(Map.of("email", "Conta já existe com esse email."));
            }

            if (accountsRepository.findOneByCellphoneNumber(cellphoneNumber) != null) {
                errors.add(Map.of("cellphoneNumber", "Conta já existe com esse número de celular."));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao acessar o banco de dados.", e);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
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
