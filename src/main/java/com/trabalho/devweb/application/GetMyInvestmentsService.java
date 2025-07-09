package com.trabalho.devweb.application;

import com.trabalho.devweb.application.interfaces.IInvestmentRepository;
import com.trabalho.devweb.domain.MyInvestment;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class GetMyInvestmentsService {
    private final IInvestmentRepository investmentRepository;

    public GetMyInvestmentsService(IInvestmentRepository investmentRepository) {
        this.investmentRepository = investmentRepository;
    }

    public List<MyInvestment> execute(String accountId) {
        return investmentRepository.findMyInvestmentsByAccountId(accountId);
    }

    public List<MyInvestment> executeAggregated(String accountId) {
        List<MyInvestment> allInvestments = investmentRepository.findMyInvestmentsByAccountId(accountId);
        Map<String, AggregatedInvestment> aggregatedMap = new LinkedHashMap<>();

        // Agregar por chave única: categoria + ano de vencimento
        for (MyInvestment investment : allInvestments) {
            String key = investment.getCategory() + "_" + investment.getMaturityDate().toLocalDate().getYear();

            if (aggregatedMap.containsKey(key)) {
                AggregatedInvestment existing = aggregatedMap.get(key);
                existing.addInvestment(investment);
            } else {
                aggregatedMap.put(key, new AggregatedInvestment(investment));
            }
        }

        // Converter de volta para List<MyInvestment>
        List<MyInvestment> result = new ArrayList<>();
        for (AggregatedInvestment agg : aggregatedMap.values()) {
            result.add(agg.toMyInvestment());
        }

        return result;
    }

    // Classe auxiliar para agregação
    private static class AggregatedInvestment {
        private String investmentTitle;
        private String category;
        private Date maturityDate;
        private BigDecimal totalAmountInvested;
        private BigDecimal totalCurrentValue;
        private BigDecimal unitPrice;
        private BigDecimal rentabilityPercent;
        private String rentabilityIndex;
        private Timestamp earliestCreatedAt;
        private int count;

        public AggregatedInvestment(MyInvestment first) {
            this.investmentTitle = first.getInvestmentTitle();
            this.category = first.getCategory();
            this.maturityDate = first.getMaturityDate();
            this.totalAmountInvested = first.getAmountInvested();
            this.totalCurrentValue = first.getCurrentValue(); // Calcula o valor atual individual
            this.unitPrice = first.getUnitPrice();
            this.rentabilityPercent = first.getRentabilityPercent();
            this.rentabilityIndex = first.getRentabilityIndex();
            this.earliestCreatedAt = first.getCreatedAt();
            this.count = 1;
        }

        public void addInvestment(MyInvestment investment) {
            this.totalAmountInvested = this.totalAmountInvested.add(investment.getAmountInvested());
            this.totalCurrentValue = this.totalCurrentValue.add(investment.getCurrentValue()); // Soma os valores atuais
                                                                                               // calculados
                                                                                               // individualmente

            // Manter a data de criação mais antiga
            if (investment.getCreatedAt().before(this.earliestCreatedAt)) {
                this.earliestCreatedAt = investment.getCreatedAt();
            }

            this.count++;
        }

        public MyInvestment toMyInvestment() {
            String title = this.investmentTitle;
            if (count > 1) {
                title += " (" + count + " aplicações)";
            }

            // Criar um MyInvestment agregado com valor atual já calculado
            return new AggregatedMyInvestment(
                    title,
                    this.category,
                    this.maturityDate,
                    this.totalAmountInvested,
                    this.totalCurrentValue, // Passar o valor atual já calculado
                    this.unitPrice,
                    this.rentabilityPercent,
                    this.rentabilityIndex,
                    this.earliestCreatedAt);
        }
    }

    // Classe especial para investimentos agregados que já têm o valor atual
    // calculado
    private static class AggregatedMyInvestment extends MyInvestment {
        private final BigDecimal preCalculatedCurrentValue;

        public AggregatedMyInvestment(String investmentTitle, String category, Date maturityDate,
                BigDecimal amountInvested, BigDecimal currentValue, BigDecimal unitPrice,
                BigDecimal rentabilityPercent, String rentabilityIndex, Timestamp createdAt) {
            super(investmentTitle, category, maturityDate, amountInvested, unitPrice,
                    rentabilityPercent, rentabilityIndex, createdAt);
            this.preCalculatedCurrentValue = currentValue;
        }

        @Override
        public BigDecimal getCurrentValue() {
            return preCalculatedCurrentValue;
        }
    }
}
