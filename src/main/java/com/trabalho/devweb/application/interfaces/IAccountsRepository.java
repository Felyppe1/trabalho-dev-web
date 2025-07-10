package com.trabalho.devweb.application.interfaces;

import com.trabalho.devweb.domain.Account;
import java.sql.SQLException;

public interface IAccountsRepository {
    void save(Account account) throws SQLException;
    void update(Account account) throws SQLException;
    Account findById(String id) throws SQLException;
    Account findOneByCpf(String cpf) throws SQLException;
    Account findOneByEmail(String email) throws SQLException;
    Account findOneByCellphoneNumber(String cellphoneNumber) throws SQLException;
    boolean checkIfAlreadyExists(String cpf, String email, String cellphoneNumber) throws SQLException;
    boolean checkIfAlreadyExistsForUpdate(String id, String email, String cellphoneNumber) throws SQLException;
}