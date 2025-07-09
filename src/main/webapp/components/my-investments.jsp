<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.trabalho.devweb.domain.MyInvestment" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    List<MyInvestment> myInvestments = (List<MyInvestment>) request.getAttribute("myInvestments");
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
%>

<div class="card">
    <div class="portfolio">
        <h1 class="portfolio__title">Minha Carteira de Investimentos</h1>
        <p class="portfolio__subtitle">Seus investimentos atuais e seu desempenho</p>
    </div>

    <div class="investments-table">
        <div class="investments-table__header">
            <div>Título</div>
            <div>Retorno Anual</div>
            <div>Valor Investido</div>
            <div>Valor Atual</div>
            <div>Vencimento</div>
            <div></div>
        </div>

        <%
            if (myInvestments != null && !myInvestments.isEmpty()) {
                for (MyInvestment investment : myInvestments) {
                    String indicatorClass = "";
                    String nameClass = "";
                    
                    if (investment.getCategory().contains("PREFIXADO")) {
                        indicatorClass = "investment__indicator--prefixado";
                        nameClass = "investment__name--prefixado";
                    } else if (investment.getCategory().contains("SELIC")) {
                        indicatorClass = "investment__indicator--selic";
                        nameClass = "investment__name--selic";
                    } else if (investment.getCategory().contains("IPCA")) {
                        indicatorClass = "investment__indicator--ipca";
                        nameClass = "investment__name--ipca";
                    } else if (investment.getCategory().contains("EDUCA")) {
                        indicatorClass = "investment__indicator--educa";
                        nameClass = "investment__name--educa";
                    } else if (investment.getCategory().contains("RENDA")) {
                        indicatorClass = "investment__indicator--renda";
                        nameClass = "investment__name--renda";
                    }
        %>
            <div class="investments-table__row">
                <div class="investment">
                    <div class="investment__indicator <%= indicatorClass %>"></div>
                    <span class="investment__name <%= nameClass %>"><%= investment.getInvestmentTitle() %></span>
                </div>
                <div class="investment__return"><%= investment.getFormattedReturn() %></div>
                <div class="investment__amount"><%= currencyFormat.format(investment.getAmountInvested()) %></div>
                <div class="investment__value"><%= currencyFormat.format(investment.getCurrentValue()) %></div>
                <div class="investment__maturity"><%= dateFormat.format(investment.getMaturityDate()) %></div>
                <a href="#" class="button button--details">Detalhes</a>
            </div>
        <%
                }
            } else {
        %>
            <div class="investments-table__row">
                <div style="grid-column: 1 / -1; text-align: center; padding: 2rem;">
                    Você ainda não possui investimentos.
                </div>
            </div>
        <%
            }
        %>
    </div>
</div>