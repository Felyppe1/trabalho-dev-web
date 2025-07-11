package com.trabalho.devweb.infrastructure.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.trabalho.devweb.domain.Account;
import com.trabalho.devweb.domain.Investment;
import com.trabalho.devweb.infrastructure.databaseconnection.PostgresConnection;
import com.trabalho.devweb.infrastructure.repositories.InvestmentsRepository;
import com.trabalho.devweb.application.BuyInvestmentService;
import com.trabalho.devweb.application.interfaces.IInvestmentRepository;

@WebServlet(name = "BuyInvestmentController", urlPatterns = "/comprar-investimento/*")
public class BuyInvestmentController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.length() <= 1) {
            session.setAttribute("error", "URL inválida");
            response.sendRedirect(request.getContextPath() + "/investimentos");
            return;
        }

        // Remover a barra inicial e pegar o id
        String investmentId = pathInfo.substring(1);

        if (investmentId.trim().isEmpty()) {
            session.setAttribute("error", "URL inválida");
            response.sendRedirect(request.getContextPath() + "/investimentos");
            return;
        }

        try {
            String[] parts = investmentId.split("-");
            if (parts.length != 2) {
                session.setAttribute("error", "Id do investimento inválido");
                response.sendRedirect(request.getContextPath() + "/investimentos");
                return;
            }

            String category = "TESOURO " + parts[0].trim().toUpperCase();
            int year;

            try {
                year = Integer.parseInt(parts[1].trim());
            } catch (NumberFormatException e) {
                session.setAttribute("error", "Ano de vencimento inválido no ID do investimento");
                response.sendRedirect(request.getContextPath() + "/investimentos");
                return;
            }

            Connection connection = PostgresConnection.getConnection();
            IInvestmentRepository investmentRepository = new InvestmentsRepository(connection);

            Investment investment = investmentRepository.findInvestmentByCategoryAndYear(category, year);

            if (investment == null) {
                session.setAttribute("error", "Investimento não encontrado: " + investmentId);
                response.sendRedirect(request.getContextPath() + "/investimentos");
                return;
            }

            request.setAttribute("investment", investment);

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Ocorreu um erro inesperado: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/investimentos");
            return;
        }

        request.getRequestDispatcher("/buy-investment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        try {
            // Pegar dados do formulário
            String amountStr = request.getParameter("amount");
            String category = request.getParameter("category");
            String yearStr = request.getParameter("year");

            // Validações básicas de entrada
            if (amountStr == null || amountStr.trim().isEmpty()) {
                request.setAttribute("error", "Valor é obrigatório");
                doGet(request, response);
                return;
            }

            if (category == null || category.trim().isEmpty()) {
                request.setAttribute("error", "Categoria do investimento é obrigatória");
                doGet(request, response);
                return;
            }

            if (yearStr == null || yearStr.trim().isEmpty()) {
                request.setAttribute("error", "Ano do investimento é obrigatório");
                doGet(request, response);
                return;
            }

            amountStr = amountStr.replace(".", "").replace(",", ".");
            BigDecimal amount = new BigDecimal(amountStr);
            int year = Integer.parseInt(yearStr);

            Connection connection = PostgresConnection.getConnection();
            BuyInvestmentService buyInvestmentService = new BuyInvestmentService(
                    connection,
                    new com.trabalho.devweb.infrastructure.repositories.AccountsRepository(connection),
                    new InvestmentsRepository(connection),
                    new com.trabalho.devweb.infrastructure.repositories.TransactionRepository(connection),
                    new com.trabalho.devweb.infrastructure.repositories.ApplicationRepository(connection));

            boolean success = buyInvestmentService.execute(account.getId(), category, year, amount);

            if (success) {
                session.setAttribute("success",
                        "Investimento no valor de R$ " + amount.toString().replace('.', ',') + " realizado com sucesso!");

                response.sendRedirect(request.getContextPath() + "/eu/investimentos");
            } else {
                request.setAttribute("error", "Falha ao processar investimento");
                doGet(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Formato de valor inválido");
            doGet(request, response);
        } catch (RuntimeException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Erro ao processar investimento: " + e.getMessage());
            doGet(request, response);
        }
    }
}
