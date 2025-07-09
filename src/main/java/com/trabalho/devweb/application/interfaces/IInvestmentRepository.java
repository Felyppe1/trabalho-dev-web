package com.trabalho.devweb.application.interfaces;

import com.trabalho.devweb.domain.Investment;
import java.util.List;

public interface IInvestmentRepository {
    List<Investment> findAvailableInvestments();
}
