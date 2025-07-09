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
import jakarta.servlet.http.HttpSession;
import com.trabalho.devweb.application.GetMyInvestmentsService;
import com.trabalho.devweb.application.interfaces.IInvestmentRepository;
import com.trabalho.devweb.infrastructure.repositories.InvestmentsRepository;
import com.trabalho.devweb.infrastructure.databaseconnection.PostgresConnection;
import com.trabalho.devweb.domain.MyInvestment;
import com.trabalho.devweb.domain.Account;

@WebServlet(name = "MyInvestmentsController", urlPatterns = "/eu/investimentos")
public class MyInvestmentsController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        request.setAttribute("uri", uri);

        HttpSession session = request.getSession(false);

        Account account = (Account) session.getAttribute("account");

        String accountId = account.getId();

        try {
            Connection connection = PostgresConnection.getConnection();
            IInvestmentRepository investmentRepository = new InvestmentsRepository(connection);
            GetMyInvestmentsService getMyInvestmentsService = new GetMyInvestmentsService(investmentRepository);
            List<MyInvestment> myInvestments = getMyInvestmentsService.executeAggregated(accountId);
            request.setAttribute("myInvestments", myInvestments);
        } catch (Exception e) {
            e.printStackTrace();
            // Em caso de erro, lista vazia
            request.setAttribute("myInvestments", new java.util.ArrayList<MyInvestment>());
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/investments.jsp");
        dispatcher.forward(request, response);
    }
}
