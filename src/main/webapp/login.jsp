<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/global.css">

</head>
<body>
    <h2>Login</h2>
    <form method="post" action="login">
        <label for="username">Usuário:</label>
        <input type="text" id="username" name="username" required><br>
        <label for="password">Senha:</label>
        <input type="password" id="password" name="password" required><br>
        <button type="submit">Entrar</button>
    </form>
    <% String error = (String) request.getAttribute("error"); %>
    <% String success = (String) request.getAttribute("success"); %>
    <div class="notification-area">
    <% if (error != null) { %>
        <div class="notification notification--error"><%= error %></div>
    <% } %>
    <% if (success != null) { %>
        <div class="notification notification--success"><%= success %></div>
    <% } %>
    </div>
    <script>
        document.querySelectorAll('.notification').forEach((el, i) => {
            setTimeout(() => {
                el.classList.add('notification--hide');
            }, 5000 + i * 300);
        });
    </script>
</body>
</html>
