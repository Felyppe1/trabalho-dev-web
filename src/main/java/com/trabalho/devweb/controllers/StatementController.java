package com.trabalho.devweb.controllers;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class StatementController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/statement.jsp");
        dispatcher.forward(request, response);
        // response.setContentType("text/html");
        // response.getWriter().println("<h1>Hello, World! Servlet funcionando!</h1>");
    }
}