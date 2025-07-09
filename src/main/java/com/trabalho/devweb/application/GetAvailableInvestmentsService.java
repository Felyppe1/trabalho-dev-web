package com.trabalho.devweb.application;

import com.trabalho.devweb.application.interfaces.IInvestmentRepository;
import com.trabalho.devweb.domain.Investment;
import java.util.List;

public class GetAvailableInvestmentsService {
    private final IInvestmentRepository investmentRepository;

    public GetAvailableInvestmentsService(IInvestmentRepository investmentRepository) {
        this.investmentRepository = investmentRepository;
    }

    public List<Investment> execute() {
        return investmentRepository.findAvailableInvestments();
    }
}
