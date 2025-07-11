package com.trabalho.devweb.application;

import com.trabalho.devweb.application.interfaces.IInvestmentRepository;
import com.trabalho.devweb.domain.MyInvestment;
import java.util.List;
import java.sql.SQLException;

public class GetMyInvestmentsService {
    private final IInvestmentRepository investmentRepository;

    public GetMyInvestmentsService(IInvestmentRepository investmentRepository) {
        this.investmentRepository = investmentRepository;
    }

    public List<MyInvestment> execute(String accountId) throws SQLException {
        return investmentRepository.findInvestmentsByAccountId(accountId);
    }
}
