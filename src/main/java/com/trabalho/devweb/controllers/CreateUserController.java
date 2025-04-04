package com.trabalho.devweb.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateUserController extends HttpServlet {
    // private final CreateUserUseCase createUserUseCase = new CreateUserUseCase(new UserRepositoryImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Redireciona para a página JSP com o formulário
        // req.getRequestDispatcher("/users/createUser.jsp").forward(req, resp);
        res.setContentType("text/html");
        res.getWriter().println("<h1>Cadastrar usuário</h1>");
    }

    // @Override
    // protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //     String name = req.getParameter("name");
    //     String email = req.getParameter("email");
    //     String password = req.getParameter("password");

    //     try {
    //         User user = createUserUseCase.execute(name, email, password);

    //         // Se a criação for bem-sucedida, redireciona para a página inicial
    //         resp.sendRedirect("/home.jsp");
    //     } catch (Exception e) {
    //         // Se houver erro, volta para a página de criação com mensagem de erro
    //         resp.sendRedirect("/users/create?error=" + e.getMessage());
    //     }
    // }
}
