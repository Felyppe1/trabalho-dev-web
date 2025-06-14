package com.trabalho.devweb.infrastructure.controllers;

import com.trabalho.devweb.domain.Account;
import com.trabalho.devweb.infrastructure.databaseconnection.PostgresConnection;
import com.trabalho.devweb.infrastructure.repositories.AccountsRepository;
import com.trabalho.devweb.application.CreateAccountService;
import com.trabalho.devweb.application.errors.BusinessRuleException;
import com.trabalho.devweb.application.errors.ValidationException;

import java.sql.Connection;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

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
            AccountsRepository accountsRepository = new AccountsRepository(connection);

            new CreateAccountService(accountsRepository).execute(
                    cpf,
                    name,
                    email,
                    password,
                    birthDate,
                    address,
                    cellphoneNumber);

            req.setAttribute("success", "Conta criada com sucesso");
            req.getRequestDispatcher("/create-account.jsp").forward(req, res);
        } catch (ValidationException e) {
            req.setAttribute("fieldErrors", e.getErrors());

            req.setAttribute("cpf", cpf);
            req.setAttribute("name", name);
            req.setAttribute("email", email);
            req.setAttribute("password", password);
            req.setAttribute("birthDate", birthDateStr);
            req.setAttribute("address", address);
            req.setAttribute("cellphoneNumber", cellphoneNumber);

            req.getRequestDispatcher("/create-account.jsp").forward(req, res);
        } catch (Exception e) {
            req.setAttribute("error", "Erro inesperado: " + e.getMessage());
            req.getRequestDispatcher("/create-account.jsp").forward(req, res);
        }
    }
}
