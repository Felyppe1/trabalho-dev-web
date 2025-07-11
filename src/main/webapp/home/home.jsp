<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="com.trabalho.devweb.domain.Account" %>
<%@ page import="com.trabalho.devweb.domain.Transaction" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/home/home.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/components/header.css">
    <title>DevBank</title>
</head>
<body>
    <%
        Account account = (Account) session.getAttribute("account");
    %>
    
    <%@ include file="../components/header.jsp" %>

    <% String successMessage = (String) request.getAttribute("successMessage"); %>
    <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
    
    <div class="notification-area">
        <% if (successMessage != null) { %>
            <div class="notification notification--success"><%= successMessage %></div>
        <% } %>
        <% if (errorMessage != null) { %>
            <div class="notification notification--error"><%= errorMessage %></div>
        <% } %>
    </div>
    
    <script>
        document.querySelectorAll('.notification').forEach((el, i) => {
            setTimeout(() => {
                el.classList.add('notification--hide');
            }, 5000 + i * 300);
        });

        // Funcionalidade do olho para mostrar/esconder informações sensíveis
        document.addEventListener('DOMContentLoaded', function() {
            const eyeIcon = document.getElementById('balance__eye');
            const balanceAmount = document.querySelector('.balance__amount');
            const accountNumber = document.querySelector('.balance__account-container > div > span:last-child');
            const transactionAmounts = document.querySelectorAll('.transactions__amount');
            const transactionNames = document.querySelectorAll('.transactions__name');
            const transactionDates = document.querySelectorAll('.transactions__date');
            
            // Estado inicial (sempre mostra as informações)
            let isHidden = false;
            
            // Função para alternar a visibilidade
            function toggleVisibility(hide) {
                if (hide) {
                    eyeIcon.classList.add('hidden');
                    balanceAmount.classList.add('hidden');
                    accountNumber.classList.add('hidden');
                    transactionAmounts.forEach(amount => amount.classList.add('hidden'));
                    transactionNames.forEach(name => name.classList.add('hidden'));
                    transactionDates.forEach(date => date.classList.add('hidden'));
                } else {
                    eyeIcon.classList.remove('hidden');
                    balanceAmount.classList.remove('hidden');
                    accountNumber.classList.remove('hidden');
                    transactionAmounts.forEach(amount => amount.classList.remove('hidden'));
                    transactionNames.forEach(name => name.classList.remove('hidden'));
                    transactionDates.forEach(date => date.classList.remove('hidden'));
                }
            }
            
            // Event listener para o clique no ícone do olho
            eyeIcon.addEventListener('click', function() {
                isHidden = !isHidden;
                
                // Aplica a mudança de visibilidade
                toggleVisibility(isHidden);
                
                // Adiciona um pequeno feedback visual
                eyeIcon.style.transform = 'scale(0.9)';
                setTimeout(() => {
                    eyeIcon.style.transform = 'scale(1)';
                }, 150);
            });
        });
    </script>
  
    <main class="main">
        <h1>Bem-vindo, <%= account.getName() %></h1>
        <div>
            <section class="card balance">
                <div class="balance__account-container">
                    <div>
                        <span>Conta Corrente</span>
                        <span><%= account.getAccountNumber() %></span>
                    </div>
                    <button id="balance__eye" type="button"></button>
                </div>
                <div class="balance__amount-container">
                    <span class="balance__amount"><%= account.getFormattedBalance() %></span>
                    <span class="balance__available">Saldo Disponível</span>
                </div>
            </section>
            
            <%@ include file="components/deposit.jsp" %>

            <section class="card transactions">
                <h2>Transações Recentes</h2>
                <ul class="transactions__list">
                    <%
                        @SuppressWarnings("unchecked")
                        List<Transaction> recentTransactions = (List<Transaction>) request.getAttribute("recentTransactions");
                        if (recentTransactions != null && !recentTransactions.isEmpty()) {
                            for (Transaction transaction : recentTransactions) {
                                String iconClass = "transactions__icon--success";
                                String amountClass = "transactions__amount";
                                String amountPrefix = "";
                                
                                // Determine transaction type and styling
                                if (transaction.isIncoming(account.getId())) {
                                    iconClass = "transactions__icon--success";
                                    amountPrefix = "+ ";
                                } else if (transaction.isOutgoing(account.getId())) {
                                    iconClass = "transactions__icon--danger";
                                    amountClass = "transactions__amount transactions__amount--danger";
                                    amountPrefix = "- ";
                                } else if (transaction.isInternal(account.getId())) {
                                    if ("DEPOSIT".equals(transaction.getType())) {
                                        iconClass = "transactions__icon--success";
                                        amountPrefix = "+ ";
                                    } else if ("WITHDRAW".equals(transaction.getType())) {
                                        iconClass = "transactions__icon--danger";
                                        amountClass = "transactions__amount transactions__amount--danger";
                                        amountPrefix = "- ";
                                    }
                                }
                    %>
                    <li class="transactions__item">
                        <div>
                            <span class="transactions__icon <%= iconClass %>">
                                <%@ include file="../icons/card-send.svg" %>
                            </span>
                            <div>
                                <span class="transactions__name"><%= transaction.getDisplayName() %></span>
                                <span class="transactions__date"><%= transaction.getFormattedDate() %></span>
                            </div>
                        </div>
                        <span class="<%= amountClass %>"><%= amountPrefix %>R$ <%= transaction.getFormattedAmount() %></span>
                    </li>
                    <%
                            }
                        } else {
                    %>
                    <li class="transactions__item">
                        <div>
                            <span class="transactions__icon transactions__icon--success">
                                <%@ include file="../icons/card-send.svg" %>
                            </span>
                            <div>
                                <span class="transactions__name">Nenhuma transação encontrada</span>
                                <span class="transactions__date">Faça sua primeira transação</span>
                            </div>
                        </div>
                        <span class="transactions__amount">R$ 0,00</span>
                    </li>
                    <%
                        }
                    %>
                </ul>
            </section>
        </div>
    </main>
  </body>

</html>
