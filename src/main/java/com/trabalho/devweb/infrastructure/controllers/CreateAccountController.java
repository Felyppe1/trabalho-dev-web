package com.trabalho.devweb.controllers;

import com.trabalho.devweb.domain.Account;
import com.trabalho.devweb.infrastructure.databaseconnection.PostgresConnection;
// import com.trabalho.devweb.infrastructure.repositories.AccountsRepository;

import java.sql.Connection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;

import java.io.IOException;

public class CreateAccountController extends HttpServlet {
    // private final CreateUserUseCase createUserUseCase = new CreateUserUseCase(new UserRepositoryImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Redireciona para a página JSP com o formulário
        // req.getRequestDispatcher("/users/createUser.jsp").forward(req, resp);
        // res.setContentType("text/html");
        // res.getWriter().println("<h1>Cadastrar usuário</h1>");
        RequestDispatcher dispatcher = req.getRequestDispatcher("/create-account.jsp");
        dispatcher.forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // String name = req.getParameter("name");
        // String email = req.getParameter("email");
        // String password = req.getParameter("password");



        try (Connection connection = PostgresConnection.getConnection()) {
            // UsersRepository usersRepository = new UsersRepository(connection);

            // User user = new User(name, email);
            
            // usersRepository.save(user);
            // User user = createUserUseCase.execute(name, email, password);

            res.sendRedirect("/criar-conta");
        } catch (Exception e) {
            // Se houver erro, volta para a página de criação com mensagem de erro
            res.sendRedirect("/criar-conta?error=" + e.getMessage());
        }
    }
}
