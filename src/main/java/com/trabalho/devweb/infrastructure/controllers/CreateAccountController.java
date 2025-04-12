package com.trabalho.devweb.infrastructure.controllers;

import com.trabalho.devweb.domain.Account;
import com.trabalho.devweb.infrastructure.databaseconnection.PostgresConnection;
import com.trabalho.devweb.infrastructure.repositories.AccountsRepository;

import java.sql.Connection;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;

import java.io.IOException;

public class CreateAccountController extends HttpServlet {
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
        String cpf = req.getParameter("cpf");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String birthDateStr = req.getParameter("birthDate"); // formato: yyyy-MM-dd
        String address = req.getParameter("address");
        String cellphoneNumber = req.getParameter("cellphoneNumber");

        LocalDate birthDate = LocalDate.parse(birthDateStr);

        try (Connection connection = PostgresConnection.getConnection()) {
            // Verificar se já tem alguma conta com email ou cpf

            AccountsRepository accountsRepository = new AccountsRepository(connection);

            // User user = new User(name, email);
            Account account = new Account(
                cpf,
                name,
                email,
                password,
                birthDate,
                address,
                cellphoneNumber
            );
            
            accountsRepository.save(account);

            res.sendRedirect(req.getContextPath() + "/criar-conta");
        } catch (Exception e) {
            // Se houver erro, volta para a página de criação com mensagem de erro
            req.setAttribute("error", e.getMessage());
            res.sendRedirect(req.getContextPath() + "/criar-conta");
        }
    }
}
