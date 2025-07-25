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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/statement-detail.css">
</head>
<body>
    <%@ include file="components/header.jsp" %>

    <h1 class="page-title">Extrato de <%= MonthName %> <%= year %></h1>
    <div class="statements">
        <div class="statements__header">
            <h1 class="statements__title">Lançamentos </h1>
            <p class="status">Última atualização em <%= nowFormatted %></p>
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
                        
                        // Determinar se é transferência enviada ou recebida baseado nos IDs
                        boolean isTransferOut = type.equals("TRANSFER") && accountId.equals(t.getOriginId());
                        boolean isTransferIn = type.equals("TRANSFER") && accountId.equals(t.getTargetId());
                        boolean isDeposit = type.equals("DEPOSIT");
                        boolean isWithdraw = type.equals("WITHDRAW");
                        boolean isInvestment = type.equals("INVESTMENT");
                        boolean isRedemption = type.equals("REDEMPTION");
                        
                        String cssClass = (isTransferOut || isWithdraw || isInvestment) ? "negative" : 
                                        (isTransferIn || isDeposit || isRedemption) ? "positive" : "";

                        String typeTransaction = "";
                        String label = "";
                        String relatedAccountId = "";
                        
                        if (isTransferOut) {
                            typeTransaction = "Transferência enviada";
                            label = "Para";
                            relatedAccountId = t.getTargetId();
                        } else if (isTransferIn) {
                            typeTransaction = "Transferência recebida";
                            label = "De";
                            relatedAccountId = t.getOriginId();
                        } else if (isDeposit) {
                            typeTransaction = "Depósito";
                            label = "";
                            relatedAccountId = "";
                        } else if (isWithdraw) {
                            typeTransaction = "Saque";
                            label = "";
                            relatedAccountId = "";
                        } else if (isInvestment) {
                            typeTransaction = "Compra de investimento";
                            label = "";
                            relatedAccountId = "";
                        } else if (isRedemption) {
                            typeTransaction = "Venda de investimento";
                            label = "";
                            relatedAccountId = "";
                        }

                        String formattedDate = t.getCreatedAt() != null
                            ? t.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                            : "Data desconhecida";
            %>

                <div class="transfer-item <%= cssClass %>">
                    <div class="info">
                        <% if (isTransferOut || isTransferIn) { %>
                            <strong><%= typeTransaction %> <%= label %> : <%= relatedAccountId %></strong>
                        <% } else { %>
                            <strong><%= typeTransaction %></strong>
                        <% } %>
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
