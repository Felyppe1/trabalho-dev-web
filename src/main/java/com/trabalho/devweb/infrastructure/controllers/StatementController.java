package com.trabalho.devweb.infrastructure.controllers;

import java.io.IOException;
import java.util.List;

import com.trabalho.devweb.domain.Account;
import com.trabalho.devweb.domain.Transaction;
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
    private TransactionRepository repository = new TransactionRepository();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Account account = (Account) request.getSession().getAttribute("account");

        if (account == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        List<Transaction> transactions = repository.findByAccountId(account.getId());
        request.setAttribute("transactions", transactions);
        request.getRequestDispatcher("/statement.jsp").forward(request, response);               
    }
}