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
                        String type = t.getType() != null ? t.getType().toUpperCase() : "";
                        String cssClass = type.equals("TRANSFER_OUT") ? "negative" :
                                        type.equals("TRANSFER_IN") ? "positive" : "";

                        String typeTransaction = type.equals("TRANSFER_OUT") ? "Transferência enviada" :
                                    type.equals("TRANSFER_IN") ? "Transferência recebida" : "";

                        String label = type.equals("TRANSFER_OUT") ? "Para" :
                                    type.equals("TRANSFER_IN") ? "De" : "";
                        

                        String relatedAccountId = type.equals("TRANSFER_OUT") ? t.getTargetId() :
                                                type.equals("TRANSFER_IN") ? t.getOriginId() : "";

                        String formattedDate = t.getCreatedAt() != null
                            ? t.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                            : "Data desconhecida";
            %>

                <div class="transfer-item <%= cssClass %>">
                    <div class="info">
                        <strong><%= typeTransaction %> <%= label %>: <%= relatedAccountId %></strong>
                        <strong>Valor: R$ <%= String.format("%.2f", t.getAmount()) %></strong> <br />
                        <span>Descrição: <%= t.getDescription() %></span>                       
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
