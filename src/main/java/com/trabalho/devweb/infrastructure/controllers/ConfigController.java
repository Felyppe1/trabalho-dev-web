package com.trabalho.devweb.infrastructure.controllers;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ConfigController", urlPatterns = "/configuracoes")
public class ConfigController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/settings.jsp");
        dispatcher.forward(request, response);
        // response.setContentType("text/html");
        // response.getWriter().println("<h1>Hello, World! Servlet funcionando!</h1>");
    }
}
