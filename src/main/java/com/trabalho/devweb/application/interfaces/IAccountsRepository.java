package com.trabalho.devweb.application.interfaces;

import com.trabalho.devweb.domain.Account;
import java.sql.SQLException;

public interface IAccountsRepository {
    void save(Account account) throws SQLException;
    boolean checkIfAlreadyExists(String cpf, String email, String cellphoneNumber) throws SQLException;
}