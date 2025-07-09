package com.trabalho.devweb.application.interfaces;

import com.trabalho.devweb.domain.Transaction;
import java.sql.SQLException;

public interface ITransactionRepository {
    void save(Transaction transaction) throws SQLException;
}
