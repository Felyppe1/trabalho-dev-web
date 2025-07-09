package com.trabalho.devweb.infrastructure.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.trabalho.devweb.application.GetAvailableInvestmentsService;
import com.trabalho.devweb.application.interfaces.IInvestmentRepository;
import com.trabalho.devweb.infrastructure.repositories.InvestmentsRepository;
import com.trabalho.devweb.infrastructure.databaseconnection.PostgresConnection;
import com.trabalho.devweb.domain.Investment;

@WebServlet(name = "InvestmentsController", urlPatterns = "/investimentos")
public class InvestmentsController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        request.setAttribute("uri", uri);

        try {
            // Inversão de dependência
            Connection connection = PostgresConnection.getConnection();
            IInvestmentRepository investmentRepository = new InvestmentsRepository(connection);
            GetAvailableInvestmentsService getAvailableInvestmentsService = new GetAvailableInvestmentsService(
                    investmentRepository);
            List<Investment> availableInvestments = getAvailableInvestmentsService.execute();
            request.setAttribute("availableInvestments", availableInvestments);
        } catch (Exception e) {
            e.printStackTrace();
            // Em caso de erro, lista vazia
            request.setAttribute("availableInvestments", new java.util.ArrayList<Investment>());
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/investments.jsp");
        dispatcher.forward(request, response);
    }
}
