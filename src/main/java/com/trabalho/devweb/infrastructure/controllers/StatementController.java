package com.trabalho.devweb.infrastructure.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import com.trabalho.devweb.domain.Account;
import com.trabalho.devweb.domain.Transaction;
import com.trabalho.devweb.infrastructure.databaseconnection.PostgresConnection;
import com.trabalho.devweb.infrastructure.repositories.TransactionRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "StatementController", urlPatterns = "/extrato")
public class StatementController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Account account = (Account) request.getSession().getAttribute("account");

        if (account == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String monthParam = request.getParameter("mes");
        String yearParam = request.getParameter("ano");
        if (monthParam == null || yearParam == null) {
            request.setAttribute("mensagem", "Parâmetros de data ausentes.");
            request.getRequestDispatcher("/statement.jsp").forward(request, response);
            return;
        }
        Integer month = Integer.parseInt(monthParam);
        Integer year = Integer.parseInt(yearParam);


        try (Connection connection = PostgresConnection.getConnection()) {
            TransactionRepository transactionRepository = new TransactionRepository(connection);

            List<Transaction> transactions = transactionRepository.findByAccountIdAndMonth(account.getId(), month, year);
            request.setAttribute("transactions", transactions);
            request.getRequestDispatcher("/statement.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/statement.jsp");
        dispatcher.forward(request, response);
    }
}