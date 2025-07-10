<%@ page import="java.util.List" %>
<%@ page import="com.trabalho.devweb.domain.Transaction" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String mesParam = request.getParameter("mes");
    String anoParam = request.getParameter("ano");
    int mes = mesParam != null ? Integer.parseInt(mesParam) : 0;
    int ano = anoParam != null ? Integer.parseInt(anoParam) : 0;

    List<Transaction> transactions = (List<Transaction>) request.getAttribute("transactions");

    String[] nomesMeses = {
        "", "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
        "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    };
    String nomeMes = (mes >= 1 && mes <= 12) ? nomesMeses[mes] : "Mês Desconhecido";
%>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Extrato de <%= nomeMes %> <%= ano %></title>
    <link rel="stylesheet" href="/global.css">
    <link rel="stylesheet" href="/extrato.css">
</head>
<body>

    <h1 class="page-title">Extrato de <%= nomeMes %> <%= ano %></h1>
year
    <div class="transfer-list">
        <%
            if (transactions == null || transactions.isEmpty()) {
        %>
            <p>❌ Nenhuma transação encontrada para esse mês.</p>
        <%
            } else {
                for (Transaction t : transactions) {
                    boolean isSent = t.getType().equalsIgnoreCase("transfer") && t.getOriginId().equals(((com.trabalho.devweb.domain.Account) session.getAttribute("account")).getId());
                    String cssClass = isSent ? "negative" : "positive";
                    String label = isSent ? "Para" : "De";
                    String formattedDate = t.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        %>

        <div class="transfer-item <%= cssClass %>">
            <div class="info">
                <strong><%= t.getType().toUpperCase() %> <%= label %>: <%= isSent ? "Conta destino" : "Conta origem" %></strong><br />
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

</body>
</html>
