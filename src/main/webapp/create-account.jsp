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
    <% String error = (String) request.getAttribute("error"); %>
    <% Map<String, String> fieldErrors = (Map<String, String>) request.getAttribute("fieldErrors"); %>
    
    <div class="notification-area">
    <% if (error != null) { %>
        <div class="notification notification--info"><%= error %></div>
    <% } %>
    <% if (fieldErrors != null && !fieldErrors.isEmpty()) { %>
        <div class="notification notification--error">Por favor, corrija os campos destacados abaixo.</div>
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
        
        <section class="card create">
            <h1 class="create__title">Criar Conta</h1>
            <p class="create__subtitle">Preencha seus dados para criar uma nova conta</p>
    
            <% 
                if (fieldErrors == null) fieldErrors = new java.util.HashMap<>();
                String nameValue = request.getAttribute("name") != null ? (String) request.getAttribute("name") : "";
                String emailValue = request.getAttribute("email") != null ? (String) request.getAttribute("email") : "";
                String passwordValue = request.getAttribute("password") != null ? (String) request.getAttribute("password") : "";
                String cpfValue = request.getAttribute("cpf") != null ? (String) request.getAttribute("cpf") : "";
                String birthDateValue = request.getAttribute("birthDate") != null ? (String) request.getAttribute("birthDate") : "";
                String addressValue = request.getAttribute("address") != null ? (String) request.getAttribute("address") : "";
                String cellphoneNumberValue = request.getAttribute("cellphoneNumber") != null ? (String) request.getAttribute("cellphoneNumber") : "";
            %>
            <form class="create__form" action="/trabalho-dev-web/criar-conta" method="post">
                <label class="create__form-label">Nome</label>
                <input class="create__form-input<%= fieldErrors.containsKey("name") ? " create__form-input--error" : "" %>" type="text" name="name" required value="<%= nameValue %>">
                <% if (fieldErrors.get("name") != null) { %>
                    <span class="create__input-error-message"><%= fieldErrors.get("name") %></span>
                <% } %>
                
                <label class="create__form-label">Email</label>
                <input class="create__form-input<%= fieldErrors.containsKey("email") ? " create__form-input--error" : "" %>" type="email" name="email" required value="<%= emailValue %>">
                <% if (fieldErrors.get("email") != null) { %>
                    <span class="create__input-error-message"><%= fieldErrors.get("email") %></span>
                <% } %>
                
                <label class="create__form-label">Senha</label>
                <input class="create__form-input<%= fieldErrors.containsKey("password") ? " create__form-input--error" : "" %>" type="password" name="password" required value="<%= passwordValue %>">
                <% if (fieldErrors.get("password") != null) { %>
                    <span class="create__input-error-message"><%= fieldErrors.get("password") %></span>
                <% } %>

                <label class="create__form-label">CPF</label>
                <input class="create__form-input<%= fieldErrors.containsKey("cpf") ? " create__form-input--error" : "" %>" type="text" name="cpf" required maxlength="11" placeholder="12345678900" value="<%= cpfValue %>">
                <% if (fieldErrors.get("cpf") != null) { %>
                    <span class="create__input-error-message"><%= fieldErrors.get("cpf") %></span>
                <% } %>

                <label class="create__form-label">Data de Nascimento</label>
                <input class="create__form-input<%= fieldErrors.containsKey("birthDate") ? " create__form-input--error" : "" %>" type="date" name="birthDate" required value="<%= birthDateValue %>">
                <% if (fieldErrors.get("birthDate") != null) { %>
                    <span class="create__input-error-message"><%= fieldErrors.get("birthDate") %></span>
                <% } %>

                <label class="create__form-label">Endereço</label>
                <input class="create__form-input<%= fieldErrors.containsKey("address") ? " create__form-input--error" : "" %>" type="text" name="address" value="<%= addressValue %>">
                <% if (fieldErrors.get("address") != null) { %>
                    <span class="create__input-error-message"><%= fieldErrors.get("address") %></span>
                <% } %>

                <label class="create__form-label">Celular</label>
                <input class="create__form-input<%= fieldErrors.containsKey("cellphoneNumber") ? " create__form-input--error" : "" %>" type="text" name="cellphoneNumber" value="<%= cellphoneNumberValue %>">
                <% if (fieldErrors.get("cellphoneNumber") != null) { %>
                    <span class="create__input-error-message"><%= fieldErrors.get("cellphoneNumber") %></span>
                <% } %>

                <button class="create__form-button" type="submit">Criar Conta</button>
            </form>

            <span class='create__login'>Já tem uma conta? <a href="">Faça login</a></span>

        </section>
    </main>
  </body>

</html>