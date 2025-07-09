package com.trabalho.devweb.application.interfaces;

import com.trabalho.devweb.domain.Investment;
import com.trabalho.devweb.domain.MyInvestment;
import java.util.List;
import java.sql.SQLException;

public interface IInvestmentRepository {
    List<Investment> findAvailableInvestments();

    List<MyInvestment> findInvestmentsByAccountId(String accountId) throws SQLException;
}
