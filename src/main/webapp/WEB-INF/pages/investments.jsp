<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.trabalho.devweb.domain.MyInvestment" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.math.RoundingMode" %>
<%
    String uri = (String) request.getAttribute("uri");
    boolean isMyInvestments = uri.contains("/eu/");
    
    // Formatador de moeda brasileira
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    
    // Calcular valores do sumário apenas para My Investments
    BigDecimal totalInvested = BigDecimal.ZERO;
    BigDecimal totalCurrentValue = BigDecimal.ZERO;
    BigDecimal expectedReturn = BigDecimal.ZERO;
    BigDecimal totalReturn = BigDecimal.ZERO;
    
    List<MyInvestment> myInvestments = (List<MyInvestment>) request.getAttribute("myInvestments");
    if (myInvestments != null && !myInvestments.isEmpty()) {
        for (MyInvestment investment : myInvestments) {
            if (investment.getAmountInvested() != null) {
                totalInvested = totalInvested.add(investment.getAmountInvested());
            }
            if (investment.getCurrentValue() != null) {
                totalCurrentValue = totalCurrentValue.add(investment.getCurrentValue());
            }
        }
        
        // Retorno esperado = valor atual - valor investido
        expectedReturn = totalCurrentValue.subtract(totalInvested);
        
        // Rentabilidade total = (retorno esperado / total investido) * 100
        if (totalInvested.compareTo(BigDecimal.ZERO) > 0) {
            totalReturn = expectedReturn.divide(totalInvested, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
        }
    }
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Investimentos - Digital Bank</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/investments.css">
</head>
<body>
    <% String error = (String) request.getAttribute("error"); %>
    <% String success = (String) request.getAttribute("success"); %>
    
    <div class="notification-area">
    <% if (success != null) { %>
        <div class="notification notification--success"><%= success %></div>
    <% } %>
    <% if (error != null) { %>
        <div class="notification notification--error"><%= error %></div>
    <% } %>
    </div>
    <script>
        document.querySelectorAll('.notification').forEach((el, i) => {
            setTimeout(() => {
                el.classList.add('notification--hide');
            }, 5000 + i * 300);
        });
    </script>
    
    <%@ include file="components/header.jsp" %>
    
    <main class="main">
        <h1>Investimentos</h1>

        <div class="investment-summary card">
            <h2>Resumo dos Investimentos</h2>
            <p class="investment-summary__subtitle">Visão geral da sua carteira de investimentos</p>
            
            <div class="investment-summary__stats">
                <div class="stat-card">
                    <span class="stat-card__label">Total Investido</span>
                    <span class="stat-card__value"><%= currencyFormat.format(totalInvested) %></span>
                </div>
                
                <div class="stat-card">
                    <span class="stat-card__label">Lucro Total</span>
                    <span class="stat-card__value stat-card__value--positive">
                        + <%= currencyFormat.format(expectedReturn) %>
                    </span>
                </div>
                
                <div class="stat-card">
                    <span class="stat-card__label">Rentabilidade Total</span>
                    <span class="stat-card__value stat-card__value--highlight"><%= totalReturn.setScale(2, RoundingMode.HALF_UP) %>%</span>
                </div>
            </div>
        </div>

        <div class="tabs">
            <a href="${pageContext.request.contextPath}/eu/investimentos"
            class="tabs__item <%= isMyInvestments ? "tabs__item--active" : "" %>">
                Meus Investimentos
            </a>

            <a href="${pageContext.request.contextPath}/investimentos"
            class="tabs__item <%= !isMyInvestments ? "tabs__item--active" : "" %>">
                Investimentos Disponíveis
            </a>
        </div>

        <%
            if (isMyInvestments) {
        %>
            <jsp:include page="components/my-investments.jsp" />
        <%
            } else {
        %>
            <jsp:include page="components/investments.jsp" />
        <%
            }
        %>

    </main>
</body>
</html>
