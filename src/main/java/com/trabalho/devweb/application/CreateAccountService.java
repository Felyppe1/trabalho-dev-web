package com.trabalho.devweb.application;

import com.trabalho.devweb.domain.Account;

import com.trabalho.devweb.application.errors.BusinessRuleException;
import com.trabalho.devweb.application.errors.ValidationException;
import com.trabalho.devweb.application.interfaces.IAccountsRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.sql.SQLException;

public class CreateAccountService {
    private IAccountsRepository accountsRepository;

    public CreateAccountService(IAccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public void execute(String cpf, String name, String email, String password, LocalDate birthDate, String address,
            String cellphoneNumber) throws BusinessRuleException, SQLException {
        Map<String, String> fieldErrors = new HashMap<>();

        try {
            if (accountsRepository.findOneByCpf(cpf) != null) {
                fieldErrors.put("cpf", "Conta já existe com esse CPF.");
            }

            if (accountsRepository.findOneByEmail(email) != null) {
                fieldErrors.put("email", "Conta já existe com esse email.");
            }

            if (accountsRepository.findOneByCellphoneNumber(cellphoneNumber) != null) {
                fieldErrors.put("cellphoneNumber", "Conta já existe com esse número de celular.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao acessar o banco de dados.", e);
        }

        if (!fieldErrors.isEmpty()) {
            throw new ValidationException(fieldErrors);
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Account account = new Account(
                cpf,
                name,
                email,
                hashedPassword,
                birthDate,
                address,
                cellphoneNumber);

        this.accountsRepository.save(account);
    }
}
