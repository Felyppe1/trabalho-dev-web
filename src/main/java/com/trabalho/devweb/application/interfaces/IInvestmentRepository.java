package com.trabalho.devweb.application.interfaces;

import com.trabalho.devweb.domain.Investment;
import com.trabalho.devweb.domain.MyInvestment;
import java.util.List;

public interface IInvestmentRepository {
    List<Investment> findAvailableInvestments();

    List<MyInvestment> findMyInvestmentsByAccountId(String accountId);
}
