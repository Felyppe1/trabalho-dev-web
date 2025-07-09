<%@ page import="java.util.List" %>
<%@ page import="com.trabalho.devweb.domain.Investment" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.math.RoundingMode" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.text.DecimalFormatSymbols" %>
<%@ page import="java.util.Locale" %>

<div class="card">
    <div class="portfolio">
        <h2 class="portfolio__title">Available Investment Options</h2>
        <p class="portfolio__subtitle">Treasury bonds and investment options</p>
    </div>

    <div class="investments-table">
        <div class="investments-table__header">
            <div>Title</div>
            <div>Annual Return</div>
            <div>Min. Investment</div>
            <div>Unit Price</div>
            <div>Maturity</div>
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
            <a href="#" class="button">Simular</a>
        </div>
        <% 
                }
            }
        %>
    </div>
</div>