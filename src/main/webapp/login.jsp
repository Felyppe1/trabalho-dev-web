<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>DevBank</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/login.css">
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
    <main class="main">
        <div class="logo">Dev<span>Bank</span></div>
        
        <section class="card login">
            <h1 class="login__title">Login</h1>
            <p class="login__subtitle">Preencha com suas credenciais para acessar sua conta</p>
            <%
                String emailValue = request.getAttribute("email") != null ? (String) request.getAttribute("email") : "";
            %>
            <form class="login__form" action="${pageContext.request.contextPath}/login" method="post">
                <label class="login__form-label">Email</label>
                <input class="login__form-input" type="email" name="email" required value="<%= emailValue %>">
                
                <label class="login__form-label">Senha</label>
                <input class="login__form-input" type="password" name="password" required>

                <button class="login__form-button" type="submit">Entrar</button>
            </form>

            <span class='login__create'>Não tem uma conta? <a href="${pageContext.request.contextPath}/criar-conta">Cadastre-se</a></span>

        </section>
    </main>
  </body>

</html>