<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>DevBank</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/create-account.css">
</head>
<body>
    <main class="main">
      <div class="logo">Dev<span>Bank</span></div>
      
      <section class="card create">
        <h1 class="create__title">Criar Conta</h1>
        <p class="create__subtitle">Preencha seus dados para criar uma nova conta</p>
  
        <% 
            List<Map<String, String>> errors = (List<Map<String, String>>) request.getAttribute("errors");
            if (errors != null) {
                for (Map<String, String> error : errors) {
                    for (Map.Entry<String, String> entry : error.entrySet()) {
        %>
            <p style="color: red;"><strong><%= entry.getKey() %>:</strong> <%= entry.getValue() %></p>
        <%
                    }
                }
            }
        %>

        <% String success = (String) request.getAttribute("success"); %>
        <% if (success != null) { %>
            <p style="color: green;"><%= success %></p>
        <% } %>

        <form class="create__form" action="/trabalho-dev-web/criar-conta" method="post">
            <label class="create__form-label">Nome</label>
            <input class="create__form-input" type="text" name="name" required value="João Silva">
            
            <label class="create__form-label">Email</label>
            <input class="create__form-input" type="email" name="email" required value="joao.silva@email.com">
            
            <label class="create__form-label">Senha</label>
            <input class="create__form-input" type="password" name="password" required value="senha123">

            <label class="create__form-label">CPF</label>
            <input class="create__form-input" type="text" name="cpf" required maxlength="11" placeholder="12345678900" value="12345678900">

            <label class="create__form-label">Data de Nascimento</label>
            <input class="create__form-input" type="date" name="birthDate" required value="1990-01-01">

            <label class="create__form-label">Endereço</label>
            <input class="create__form-input" type="text" name="address" value="Rua ABC, 123">

            <label class="create__form-label">Celular</label>
            <input class="create__form-input" type="text" name="cellphoneNumber" value="11987654321">

            <button class="create__form-button" type="submit">Criar Conta</button>
        </form>

        <span class='create__login'>Já tem uma conta? <a href="">Faça login</a></span>

      </section>
    </main>
  </body>

</html>