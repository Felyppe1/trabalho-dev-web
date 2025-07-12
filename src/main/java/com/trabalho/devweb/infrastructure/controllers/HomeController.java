package com.trabalho.devweb.infrastructure.controllers;

import com.trabalho.devweb.domain.Account;
import com.trabalho.devweb.domain.Transaction;
import com.trabalho.devweb.infrastructure.databaseconnection.PostgresConnection;
import com.trabalho.devweb.infrastructure.repositories.TransactionRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "HomeController", urlPatterns = "/home")
public class HomeController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        // Preserve action parameter for tab highlighting
        String action = request.getParameter("action");
        if (action != null) {
            request.setAttribute("action", action);
        }

        if (account != null) {
            try (Connection connection = PostgresConnection.getConnection()) {
                TransactionRepository transactionRepository = new TransactionRepository(connection);
                List<Transaction> recentTransactions = transactionRepository.findRecentByAccountId(account.getId(), 5);
                request.setAttribute("recentTransactions", recentTransactions);
            } catch (SQLException e) {
                e.printStackTrace();
                // Log error but continue - transactions section will be empty
            }
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/home/home.jsp");
        dispatcher.forward(request, response);
    }
}