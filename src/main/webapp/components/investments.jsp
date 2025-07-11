<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.trabalho.devweb.domain.Investment" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.math.RoundingMode" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.text.DecimalFormatSymbols" %>
<%@ page import="java.util.Locale" %>

<div class="card">
    <div class="portfolio">
        <h2 class="portfolio__title">Opções de Investimento Disponíveis</h2>
        <p class="portfolio__subtitle">Títulos do Tesouro e opções de investimento</p>
    </div>

    <div class="investments-table">
        <div class="investments-table__header">
            <div>Título</div>
            <div>Retorno Anual</div>
            <div>Investimento Mín.</div>
            <div>Preço Unitário</div>
            <div>Vencimento</div>
            <div></div>
        </div>
        <% 
            List<Investment> availableInvestments = (List<Investment>) request.getAttribute("availableInvestments");
            if (availableInvestments != null) {
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
                symbols.setDecimalSeparator(',');
                symbols.setGroupingSeparator('.');
                DecimalFormat currencyFormat = new DecimalFormat("#,##0.00", symbols);
                
                for (Investment inv : availableInvestments) {
                    // Criar ID do investimento no formato CATEGORY-YEAR (removendo "TESOURO " da category)
                    String category = inv.getCategory().replace("TESOURO ", "");
                    int year = inv.getExpiration().toLocalDate().getYear();
                    String investmentId = category + "-" + year;
        %>
        <div class="investments-table__row">
            <div class="investment">
                <div class="investment__indicator investment__indicator--<%= inv.getCategory().toLowerCase().contains("renda") ? "renda" : inv.getCategory().toLowerCase().contains("educa") ? "educa" : inv.getCategory().toLowerCase().contains("prefixado") ? "prefixado" : inv.getCategory().toLowerCase().contains("selic") ? "selic" : "ipca" %>"></div>
                <div class="investment__details">
                    <span class="investment__name investment__name--<%= inv.getCategory().toLowerCase().contains("renda") ? "renda" : inv.getCategory().toLowerCase().contains("educa") ? "educa" : inv.getCategory().toLowerCase().contains("prefixado") ? "prefixado" : inv.getCategory().toLowerCase().contains("selic") ? "selic" : "ipca" %>"> <%= inv.getCategory() %> <%= inv.getExpiration().toLocalDate().getYear() %></span>
                </div>
            </div>
            <div class="investment__return">
                <%= inv.getRentabilityIndex() != null ? inv.getRentabilityIndex() + " + " : "" %><%= inv.getRentabilityPercent().stripTrailingZeros().toPlainString().replace('.', ',') %>%
            </div>
            <div class="investment__amount">R$ <%= currencyFormat.format(inv.getUnitPrice().multiply(new java.math.BigDecimal("0.01"))) %></div>
            <div class="investment__price">R$ <%= currencyFormat.format(inv.getUnitPrice()) %></div>
            <div class="investment__maturity"><%= new SimpleDateFormat("dd/MM/yyyy").format(inv.getExpiration()) %></div>
            <a href="${pageContext.request.contextPath}/comprar-investimento/<%= investmentId %>" class="button">Investir</a>
        </div>
        <% 
                }
            }
        %>
    </div>
</div>