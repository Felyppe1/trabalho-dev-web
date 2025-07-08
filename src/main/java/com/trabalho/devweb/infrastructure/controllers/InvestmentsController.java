package com.trabalho.devweb.infrastructure.controllers;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "InvestmentsController", urlPatterns = "/investimentos")
public class InvestmentsController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String uri = request.getRequestURI();
        request.setAttribute("uri", uri);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/investments.jsp");
        dispatcher.forward(request, response);
    }
}
