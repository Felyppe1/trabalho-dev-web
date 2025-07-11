<%@ page import="java.util.List" %>
<%@ page import="com.trabalho.devweb.domain.Transaction" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String monthParam = request.getParameter("month");
    String yearParam = request.getParameter("year");
    int month = monthParam != null ? Integer.parseInt(monthParam) : 0;
    int year = yearParam != null ? Integer.parseInt(yearParam) : 0;

    List<Transaction> transactions = (List<Transaction>) request.getAttribute("transactions");

    String[] MonthsNames = {
        "", "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
        "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    };
    String MonthName = (month >= 1 && month <= 12) ? MonthsNames[month] : "Mês Desconhecido";
    
    String nowFormatted = LocalDateTime.now(java.time.ZoneId.of("America/Sao_Paulo"))
        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
%>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Extrato de <%= MonthName %> <%= year %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/global.css">    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/components/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/statement-detail.css">
</head>
<body>
    <%@ include file="components/header.jsp" %>

    <h1 class="page-title">Extrato de <%= MonthName %> <%= year %></h1>
    <div class="statements">
        <div class="statements__header">
            <h1 class="statements__title">Extrato de <%= MonthName %> <%= year %></h1>
            <p class="status">Gerado em <%= nowFormatted %></p>
        </div>
        <div class="transfer-list">
            <%
                if (transactions == null || transactions.isEmpty()) {
            %>
                <p>❌ Nenhuma transação encontrada para esse mês.</p>
            <%
                } else {
                    String accountId = ((com.trabalho.devweb.domain.Account) session.getAttribute("account")).getId();

                    for (Transaction t : transactions) {
                        String tipo = t.getType().toUpperCase();
                        String cssClass = tipo.equals("TRANSFER_OUT") ? "negative" : tipo.equals("TRANSFER_IN") ? "positive" : "";
                        String label = tipo.equals("TRANSFER_OUT") ? "Para" : tipo.equals("TRANSFER_IN") ? "De" : "";
                        String formattedDate = t.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                %>

                <div class="transfer-item <%= cssClass %>">
                    <div class="info">
                        <strong><%= tipo %> <%= label %>: Conta <%= label.toLowerCase() %> </strong><br />
                        <span>Valor: R$ <%= String.format("%.2f", t.getAmount()) %></span><br />
                        <span class="status">✔️ Concluída · <%= formattedDate %></span>
                    </div>
                    <div>
                        Saldo após: R$ <%= String.format("%.2f", t.getBalanceAfter()) %>
                    </div>
                </div>


            <%
                    }
                }
            %>
        </div>
    </div>
</body>
</html>
