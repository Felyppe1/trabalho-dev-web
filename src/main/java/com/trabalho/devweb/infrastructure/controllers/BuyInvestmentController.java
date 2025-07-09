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

        // Pegar account da sessão (middleware já verificou se está logada)
        // HttpSession session = request.getSession();
        // Account account = (Account) session.getAttribute("account");

        // try {
        // // Pegar dados do formulário
        // String amountStr = request.getParameter("amount");

        // // Extrair o ID do investimento do path
        // String pathInfo = request.getPathInfo();
        // if (pathInfo == null || pathInfo.length() <= 1) {
        // request.setAttribute("error", "Investment ID is required");
        // doGet(request, response);
        // return;
        // }

        // String investmentId = pathInfo.substring(1);

        // if (amountStr == null || amountStr.trim().isEmpty()) {
        // request.setAttribute("error", "Amount is required");
        // doGet(request, response);
        // return;
        // }

        // if (investmentId.trim().isEmpty()) {
        // request.setAttribute("error", "Investment ID is required");
        // doGet(request, response);
        // return;
        // }

        // // Converter string de valor para BigDecimal
        // // Remove pontos de milhar e substitui vírgula por ponto
        // amountStr = amountStr.replace(".", "").replace(",", ".");
        // BigDecimal amount = new BigDecimal(amountStr);

        // // Validar valor mínimo
        // BigDecimal minimumAmount = new BigDecimal("6.99");
        // if (amount.compareTo(minimumAmount) < 0) {
        // request.setAttribute("error", "Minimum investment amount is R$ 6,99");
        // doGet(request, response);
        // return;
        // }

        // // Verificar se o usuário tem saldo suficiente
        // BigDecimal accountBalance = account.getBalance();
        // if (accountBalance.compareTo(amount) < 0) {
        // request.setAttribute("error", "Insufficient balance");
        // doGet(request, response);
        // return;
        // }

        // // Separar category e year do ID
        // String[] parts = investmentId.split("-");
        // if (parts.length != 2) {
        // request.setAttribute("error", "Invalid investment ID format");
        // doGet(request, response);
        // return;
        // }

        // String category = parts[0].trim().toUpperCase();
        // int year;

        // try {
        // year = Integer.parseInt(parts[1].trim());
        // } catch (NumberFormatException e) {
        // request.setAttribute("error", "Invalid year in investment ID");
        // doGet(request, response);
        // return;
        // }

        // // Buscar o investimento para verificar se existe
        // Connection connection = PostgresConnection.getConnection();
        // IInvestmentRepository investmentRepository = new
        // InvestmentsRepository(connection);

        // Investment investment =
        // investmentRepository.findInvestmentByCategoryAndYear(category, year);

        // if (investment == null) {
        // request.setAttribute("error", "Investment not found");
        // doGet(request, response);
        // return;
        // }

        // // Executar a compra do investimento usando category separada
        // BuyInvestmentService buyInvestmentService = new
        // BuyInvestmentService(connection);
        // boolean success = buyInvestmentService.execute(account, category, amount);

        // if (success) {
        // // Atualizar o saldo da conta na sessão
        // BigDecimal newBalance = account.getBalance().subtract(amount);
        // account.setBalance(newBalance);
        // session.setAttribute("account", account);

        // // Adicionar mensagem de sucesso
        // session.setAttribute("successMessage",
        // "Investment purchased successfully! Amount: R$ " + amount.toString());

        // // Redirecionar para a página de investimentos
        // response.sendRedirect("investimentos");
        // } else {
        // request.setAttribute("error", "Failed to purchase investment");
        // doGet(request, response);
        // }

        // } catch (NumberFormatException e) {
        // request.setAttribute("error", "Invalid amount format");
        // doGet(request, response);
        // } catch (Exception e) {
        // e.printStackTrace();
        // request.setAttribute("error", "Error processing investment purchase: " +
        // e.getMessage());
        // doGet(request, response);
        // }
    }
}
