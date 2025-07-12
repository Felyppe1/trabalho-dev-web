<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Login - BankX</title>
  <style>
    * {
      box-sizing: border-box;
      font-family: sans-serif;
    }

    body {
      background-color: #f9fafb;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
      margin: 0;
    }

    .container {
      background: white;
      border-radius: 10px;
      padding: 2rem;
      width: 100%;
      max-width: 400px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }

    h1 {
      color: #3b82f6;
      text-align: center;
      margin-bottom: 1rem;
    }

    h2 {
      text-align: center;
      font-size: 1.5rem;
      margin-bottom: 0.5rem;
    }

    p.subtitle {
      text-align: center;
      color: #6b7280;
      font-size: 0.95rem;
      margin-bottom: 1.5rem;
    }

    label {
      display: block;
      margin-bottom: 0.25rem;
      font-weight: 600;
    }

    input[type="email"],
    input[type="password"] {
      width: 100%;
      padding: 0.75rem;
      border: 2px solid #ef4444;
      border-radius: 6px;
      margin-bottom: 0.25rem;
    }

    .error {
      color: #ef4444;
      font-size: 0.875rem;
      margin-bottom: 1rem;
    }

    .password-container {
      position: relative;
    }

    .password-container input {
      padding-right: 3rem;
    }

    .show-toggle {
      position: absolute;
      right: 0.75rem;
      top: 50%;
      transform: translateY(-50%);
      font-size: 0.875rem;
      cursor: pointer;
      color: #374151;
    }

    button {
      width: 100%;
      background-color: #3b82f6;
      color: white;
      border: none;
      padding: 0.75rem;
      border-radius: 6px;
      font-size: 1rem;
      cursor: pointer;
      margin-top: 1rem;
    }

    .signup {
      text-align: center;
      margin-top: 1rem;
      font-size: 0.875rem;
    }

    .signup a {
      color: #3b82f6;
      text-decoration: none;
      font-weight: 600;
    }
  </style>
</head>
<body>
  <div class="container">
    <h1>BankX</h1>
    <h2>Login</h2>
    <p class="subtitle">Entre com suas credenciais para acessar sua conta</p>
    
    <form>
      <label for="email">Email</label>
      <input type="email" id="email" placeholder="seu@email.com" required>
      <div class="error">Email é obrigatório</div>

      <label for="senha">Senha</label>
      <div class="password-container">
        <input type="password" id="senha" required>
        <span class="show-toggle" onclick="togglePassword()">Mostrar</span>
      </div>
      <div class="error">Senha é obrigatória</div>

      <button type="submit">➜ Entrar</button>
    </form>

    <div class="signup">
      Não tem uma conta? <a href="#">Cadastre-se</a>
    </div>
  </div>

  <script>
    function togglePassword() {
      const senha = document.getElementById("senha");
      const toggle = document.querySelector(".show-toggle");
      if (senha.type === "password") {
        senha.type = "text";
        toggle.textContent = "Esconder";
      } else {
        senha.type = "password";
        toggle.textContent = "Mostrar";
      }
    }
  </script>
</body>
</html>
