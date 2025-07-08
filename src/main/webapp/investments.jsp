<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String uri = (String) request.getAttribute("uri");
    boolean isMyInvestments = uri.contains("/eu/");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Investimentos - Digital Bank</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/components/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/components/investment-summary.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/my-investments.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/investments.css">
</head>
<body>
    <%@ include file="components/header.jsp" %>
    
    <main class="main">
        <h1>Investments</h1>

        <div class="investment-summary card">
            <h2>Investment Summary</h2>
            <p class="investment-summary__subtitle">Overview of your investment portfolio</p>
            
            <div class="investment-summary__stats">
                <div class="stat-card">
                    <span class="stat-card__label">Total Invested</span>
                    <span class="stat-card__value">R$ 16,926.49</span>
                </div>
                
                <div class="stat-card">
                    <span class="stat-card__label">Expected Return</span>
                    <span class="stat-card__value stat-card__value--positive">+ R$ 2,458.70</span>
                </div>
                
                <div class="stat-card">
                    <span class="stat-card__label">Average Annual Return</span>
                    <span class="stat-card__value stat-card__value--highlight">13.78%</span>
                </div>
            </div>
        </div>

        <div class="tabs">
            <a href="${pageContext.request.contextPath}/eu/investimentos"
            class="tabs__item <%= isMyInvestments ? "tabs__item--active" : "" %>">
                My Investments
            </a>

            <a href="${pageContext.request.contextPath}/investimentos"
            class="tabs__item <%= !isMyInvestments ? "tabs__item--active" : "" %>">
                Available Investments
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
