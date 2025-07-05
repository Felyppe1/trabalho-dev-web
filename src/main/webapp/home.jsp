<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="com.trabalho.devweb.domain.Account" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/home.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/components/header.css">
    <title>DevBank</title>
</head>
<body>
    <%
        Account account = (Account) session.getAttribute("account");
    %>
    
    <%@ include file="components/header.jsp" %>
  
    <main class="main">
        <h1 class="title">Bem-vindo, <%= account.getName() %></h1>
        <div>
            <section class="card balance">
                <div class="balance__account-container">
                    <div>
                        <span>Conta Corrente</span>
                        <span><%= account.getAccountNumber() %></span>
                    </div>
                    <span></span>
                </div>
                <div class="balance__amount-container">
                    <span class="balance__amount">R$ <%= account.getBalance() %></span>
                    <span class="balance__available">Saldo Disponível</span>
                </div>
                <div class="balance__updated-container">
                    <button class="balance__button button button--outline">Adicionar Dinheiro</button>
                    <div>
                        <span>
                            Atualizado
                        </span>
                        <span>
                            Hoje, 13:31
                        </span>
                    </div>
                </div>
            </section>
            <section class="card transactions">
                <h2 class="transactions__title">Transações recentes</h2>
                <ul class="transactions__list">
                    <li class="transactions__item">
                        <div>
                            <span class="transactions__icon transactions__icon--success">
                                <%@ include file="icons/card-send.svg" %>
                            </span>
                            <div>
                                <span class="transactions__name">Depósito de Salário</span>
                                <span class="transactions__date">Ontem, 14:34</span>
                            </div>
                        </div>
                        <span class="transactions__amount">+ R$ 3.200,00</span>
                    </li>
                    <li class="transactions__item">
                        <div>
                            <span class="transactions__icon transactions__icon--danger">
                                <%@ include file="icons/card-send.svg" %>
                            </span>
                            <div>
                                <span class="transactions__name">Depósito de Salário</span>
                                <span class="transactions__date">Ontem, 14:34</span>
                            </div>
                        </div>
                        <span class="transactions__amount transactions__amount--danger">+ R$ 3.200,00</span>
                    </li>
                    <li class="transactions__item">
                        <div>
                            <span class="transactions__icon transactions__icon--success">
                                <%@ include file="icons/card-send.svg" %>
                            </span>
                            <div>
                                <span class="transactions__name">Depósito de Salário</span>
                                <span class="transactions__date">Ontem, 14:34</span>
                            </div>
                        </div>
                        <span class="transactions__amount">+ R$ 3.200,00</span>
                    </li>
                    <li class="transactions__item">
                        <div>
                            <span class="transactions__icon transactions__icon--danger">
                                <%@ include file="icons/card-send.svg" %>
                            </span>
                            <div>
                                <span class="transactions__name">Depósito de Salário</span>
                                <span class="transactions__date">Ontem, 14:34</span>
                            </div>
                        </div>
                        <span class="transactions__amount transactions__amount--danger">+ R$ 3.200,00</span>
                    </li>
                    <li class="transactions__item">
                        <div>
                            <span class="transactions__icon transactions__icon--danger">
                                <%@ include file="icons/card-send.svg" %>
                            </span>
                            <div>
                                <span class="transactions__name">Depósito de Salário</span>
                                <span class="transactions__date">Ontem, 14:34</span>
                            </div>
                        </div>
                        <span class="transactions__amount transactions__amount--danger">+ R$ 3.200,00</span>
                    </li>
            </section>
        </div>
    </main>
  </body>

</html>