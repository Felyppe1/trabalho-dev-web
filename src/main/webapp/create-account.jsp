<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/create-account.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/components/header.css">
    <title>DevBank</title>
</head>
<body>
    <%@ include file="components/header.jsp" %>
  
    <main class="page">
      <section class="card transfer">
        <h1 class="transfer__title">Transfer Money</h1>
        <p class="transfer__subtitle">Transfer money between your accounts or to someone else.</p>
  
        <% String error = (String) request.getAttribute("error"); %>
        <% if (error != null) { %>
            <p style="color: red;"><%= error %></p>
        <% } %>

        <% String success = (String) request.getAttribute("success"); %>
        <% if (success != null) { %>
            <p style="color: green;"><%= success %></p>
        <% } %>

        <form class="account__form" action="/trabalho-dev-web/criar-conta" method="post">
            <label class="account__form-label">CPF</label>
            <input class="account__form-input" type="text" name="cpf" required maxlength="11" placeholder="12345678900" value="12345678900">

            <label class="account__form-label">Nome</label>
            <input class="account__form-input" type="text" name="name" required value="João Silva">

            <label class="account__form-label">Email</label>
            <input class="account__form-input" type="email" name="email" required value="joao.silva@email.com">

            <label class="account__form-label">Senha</label>
            <input class="account__form-input" type="password" name="password" required value="senha123">

            <label class="account__form-label">Data de Nascimento</label>
            <input class="account__form-input" type="date" name="birthDate" required value="1990-01-01">

            <label class="account__form-label">Endereço</label>
            <input class="account__form-input" type="text" name="address" value="Rua ABC, 123">

            <label class="account__form-label">Celular</label>
            <input class="account__form-input" type="text" name="cellphoneNumber" value="11987654321">

            <button class="account__form-button" type="submit">Criar Conta</button>
        </form>

      </section>
  
      <aside class="card recipients">
        <h2 class="recipients__title">Recent Recipients</h2>
  
        <div class="recipients__list">
            <div class="recipients__card">
                <span>John Doe</span>
                <span class="recipients__bank">Chase Bank - ****3456</span>
            </div>
            <div class="recipients__card">
                <span>Jane Smith</span>
                <span class="recipients__bank">Bank of America - ****5432</span>
            </div>
            <div class="recipients__card">
                <span>Robert Johnson</span>
                <span class="recipients__bank">Wells Fargo - ****3210</span>
            </div>
        </div>
      </aside>
    </main>
  </body>

</html>