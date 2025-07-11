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
import com.trabalho.devweb.application.GetAvailableInvestmentsService;
import com.trabalho.devweb.application.GetMyInvestmentsService;
import com.trabalho.devweb.application.interfaces.IInvestmentRepository;
import com.trabalho.devweb.infrastructure.repositories.InvestmentsRepository;
import com.trabalho.devweb.infrastructure.databaseconnection.PostgresConnection;
import com.trabalho.devweb.domain.Investment;
import com.trabalho.devweb.domain.MyInvestment;
import com.trabalho.devweb.domain.Account;

@WebServlet(name = "InvestmentsController", urlPatterns = "/investimentos")
public class InvestmentsController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        request.setAttribute("uri", uri);

        HttpSession session = request.getSession(false);
        Account account = (Account) session.getAttribute("account");

        String error = (String) session.getAttribute("error");
        if (error != null) {
            request.setAttribute("error", error);
            session.removeAttribute("error");
        }

        try (Connection connection = PostgresConnection.getConnection()) {
            IInvestmentRepository investmentRepository = new InvestmentsRepository(connection);

            GetAvailableInvestmentsService getAvailableInvestmentsService = new GetAvailableInvestmentsService(
                    investmentRepository);
            List<Investment> availableInvestments = getAvailableInvestmentsService.execute();
            request.setAttribute("availableInvestments", availableInvestments);

            String accountId = account.getId();
            GetMyInvestmentsService getMyInvestmentsService = new GetMyInvestmentsService(investmentRepository);
            List<MyInvestment> myInvestments = getMyInvestmentsService.execute(accountId);
            request.setAttribute("myInvestments", myInvestments);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("availableInvestments", new java.util.ArrayList<Investment>());
            request.setAttribute("myInvestments", new java.util.ArrayList<MyInvestment>());
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/investments.jsp");
        dispatcher.forward(request, response);
    }
}
