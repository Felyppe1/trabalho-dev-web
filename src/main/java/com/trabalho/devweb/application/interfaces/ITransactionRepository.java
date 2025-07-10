package com.trabalho.devweb.application.interfaces;

import com.trabalho.devweb.domain.Transaction;
import java.sql.SQLException;
import java.util.List;

public interface ITransactionRepository {
    void save(Transaction transaction) throws SQLException;

    List<Transaction> findByAccountId(String accountId);
}
