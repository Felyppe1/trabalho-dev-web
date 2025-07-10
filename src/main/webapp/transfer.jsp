<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.trabalho.devweb.domain.Transfer" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
    List<Transfer> recentTransfers = (List<Transfer>) request.getAttribute("recentTransfers");
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>DevBank - Realizar Transferência</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/transfer.css">
</head>
<body>
 <div class="back-button-container">
    <a href="transferencias" class="back-button">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M15 19l-7-7 7-7"/>
        </svg>
    </a>
</div>

<main class="page">
    <section class="card transfer">
        <h1 class="transfer__title">Realizar Transferência</h1>
        <p class="transfer__subtitle">Transfira dinheiro para outra conta usando o CPF.</p>

        <% if (error != null) { %>
            <p style="color:red;"><%= error %></p>
        <% } else if (success != null) { %>
            <p style="color:green;">Transferência realizada com sucesso!</p>
        <% } %>

        <form class="transfer__form" method="post" action="transferir">
            <label class="transfer__form-label">CPF do destinatário</label>
            <input class="transfer__form-input" type="text" name="cpf" placeholder="Digite o CPF do destinatário" required />

            <label class="transfer__form-label">Valor</label>
            <input class="transfer__form-input"
                   type="text"
                   name="amount"
                   placeholder="R$ 0.00"
                   pattern="^\d+(\.\d{1,2})?$"
                   title="Digite o valor com ponto como separador decimal. Ex: 200.00"
                   required />

            <label class="transfer__form-label">Descrição (opcional)</label>
            <input class="transfer__form-input" type="text" name="description" placeholder="Motivo da transferência" />

            <button class="transfer__form-button" type="submit">Transferir</button>
        </form>
    </section>

    <aside class="card recipients">
        <h2 class="recipients__title">Últimos Destinatários</h2>

        <div class="recipients__list">
            <% if (recentTransfers != null && !recentTransfers.isEmpty()) {
                for (Transfer t : recentTransfers) {
                    String number = t.getTargetAccountNumber();
                    String masked = number.length() >= 4 ? "****" + number.substring(number.length() - 4) : number;
            %>
                <div class="recipients__card">
                    <span><%= t.getTargetName() %></span>
                    <span class="recipients__bank">Conta - <%= masked %></span>
                    <span class="recipients__bank">R$ <%= String.format("%.2f", t.getAmount()) %> · <%= sdf.format(java.sql.Timestamp.valueOf(t.getCreatedAt())) %></span>
                </div>
            <% }
            } else { %>
                <p>Nenhuma transferência recente.</p>
            <% } %>
        </div>
    </aside>
</main>
</body>
</html>
