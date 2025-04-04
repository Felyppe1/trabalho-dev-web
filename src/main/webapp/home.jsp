<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Página Inicial</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            text-align: center;
            padding: 50px;
        }
        .container {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            display: inline-block;
        }
        h1 {
            color: #333;
        }
    </style>
</head>
<body>

    <div class="container">
        <h1>Bem-vindo à Página Inicial</h1>
        <p>Esta é a página renderizada pelo <strong>home.jsp</strong> após o redirecionamento.</p>
        
        <form action="home" method="get">
            <button type="submit">Recarregar Página</button>
        </form>

        <a href="/cadastrar">Cadastrar</a>
    </div>

</body>
</html>
