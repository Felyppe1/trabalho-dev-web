<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.trabalho.devweb.domain.Transfer" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
    List<Transfer> recentTransfers = (List<Transfer>) request.getAttribute("recentTransfers");
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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/components/header.css">
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

        <form class="transfer__form" method="post" action="transferir">
            <label class="transfer__form-label">CPF do destinatário</label>
            <input class="transfer__form-input" type="text" name="cpf" placeholder="Digite o CPF do destinatário" required />

            <label class="transfer__form-label">Valor</label>
            <input class="transfer__form-input"
                   type="text"
                   name="amount"
                   id="amount"
                   placeholder="R$ 0,00"
                   title="Digite o valor que deseja transferir"
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

    <script>
        document.getElementById('amount').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, '');
            value = (value / 100).toLocaleString('pt-BR', {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
            });
            e.target.value = value;
        });
    </script>
</main>
</body>
</html>
