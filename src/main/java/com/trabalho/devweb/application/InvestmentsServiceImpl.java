package com.trabalho.devweb.application;

import com.trabalho.devweb.application.interfaces.InvestmentsService;
import com.trabalho.devweb.application.interfaces.InvestmentsRepository;
import com.trabalho.devweb.domain.Investment;
import java.util.List;

public class InvestmentsServiceImpl implements InvestmentsService {
    private final InvestmentsRepository investmentsRepository;

    public InvestmentsServiceImpl(InvestmentsRepository investmentsRepository) {
        this.investmentsRepository = investmentsRepository;
    }

    @Override
    public List<Investment> getAvailableInvestments() {
        return investmentsRepository.findAvailableInvestments();
    }
}
