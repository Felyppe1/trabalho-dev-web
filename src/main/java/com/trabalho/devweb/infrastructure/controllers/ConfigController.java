package com.trabalho.devweb.infrastructure.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.trabalho.devweb.application.UpdateAccountService;
import com.trabalho.devweb.application.UpdateAccountService.UpdateAccountResult;
import com.trabalho.devweb.domain.Account;
import com.trabalho.devweb.infrastructure.repositories.AccountsRepository;
import com.trabalho.devweb.infrastructure.databaseconnection.PostgresConnection;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ConfigController", urlPatterns = "/configuracoes")
public class ConfigController extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/settings/settings.jsp");
        dispatcher.forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        
        if (account == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        String fullName = (firstName != null ? firstName.trim() : "") + 
                         (lastName != null && !lastName.trim().isEmpty() ? " " + lastName.trim() : "");
        
        try {
  
            Connection connection = PostgresConnection.getConnection();
            AccountsRepository accountsRepository = new AccountsRepository(connection);
            UpdateAccountService updateService = new UpdateAccountService(accountsRepository);
            UpdateAccountResult result = updateService.execute(
                account.getId(), 
                fullName, 
                email, 
                address, 
                phone
            );
            
            if (result.isSuccess()) {
                session.setAttribute("account", result.getAccount());
                request.setAttribute("successMessage", result.getMessage());
            } else {
                request.setAttribute("errorMessage", result.getMessage());
            }
            
            connection.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erro interno do servidor. Tente novamente.");
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/settings/settings.jsp");
        dispatcher.forward(request, response);
    }
}
