package com.trabalho.devweb.infrastructure.controllers;

import java.io.IOException;
import java.math.BigDecimal;
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
import com.trabalho.devweb.application.RedeemInvestmentService;
import com.trabalho.devweb.application.interfaces.IAccountsRepository;
import com.trabalho.devweb.application.interfaces.IApplicationRepository;
import com.trabalho.devweb.application.interfaces.IInvestmentRepository;
import com.trabalho.devweb.application.interfaces.ITransactionRepository;
import com.trabalho.devweb.infrastructure.repositories.AccountsRepository;
import com.trabalho.devweb.infrastructure.repositories.ApplicationRepository;
import com.trabalho.devweb.infrastructure.repositories.InvestmentsRepository;
import com.trabalho.devweb.infrastructure.repositories.TransactionRepository;
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

        String success = (String) session.getAttribute("success");
        if (success != null) {
            request.setAttribute("success", success);
            session.removeAttribute("success");
        }

        String error = (String) session.getAttribute("error");
        if (error != null) {
            request.setAttribute("error", error);
            session.removeAttribute("error");
        }

        try {
            Connection connection = PostgresConnection.getConnection();
            IInvestmentRepository investmentRepository = new InvestmentsRepository(connection);
            GetMyInvestmentsService getMyInvestmentsService = new GetMyInvestmentsService(investmentRepository);
            List<MyInvestment> myInvestments = getMyInvestmentsService.execute(accountId);
            request.setAttribute("myInvestments", myInvestments);
        } catch (Exception e) {
            e.printStackTrace();
            // Em caso de erro, lista vazia
            request.setAttribute("myInvestments", new java.util.ArrayList<MyInvestment>());
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/investments.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Account account = (Account) session.getAttribute("account");

        try {
            // Obter parâmetros da requisição
            String amountStr = request.getParameter("amount");
            String category = request.getParameter("category");
            String yearStr = request.getParameter("year");

            if (amountStr == null || category == null || yearStr == null) {
                throw new RuntimeException("Parâmetros obrigatórios não informados");
            }

            BigDecimal amount = new BigDecimal(amountStr.replace(",", "."));
            int year = Integer.parseInt(yearStr);

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Valor deve ser maior que zero");
            }

            // Configurar repositórios e serviço
            Connection connection = PostgresConnection.getConnection();
            IAccountsRepository accountRepository = new AccountsRepository(connection);
            IInvestmentRepository investmentRepository = new InvestmentsRepository(connection);
            IApplicationRepository applicationRepository = new ApplicationRepository(connection);
            ITransactionRepository transactionRepository = new TransactionRepository(connection);

            RedeemInvestmentService redeemService = new RedeemInvestmentService(
                    connection,
                    accountRepository,
                    investmentRepository,
                    transactionRepository,
                    applicationRepository);

            // Executar resgate
            boolean success = redeemService.execute(account.getId(), category, year, amount);

            if (success) {
                session.setAttribute("success", String.format("Resgate de R$ %.2f realizado com sucesso!", amount));
            } else {
                session.setAttribute("error", "Erro interno no processamento do resgate");
            }

        } catch (NumberFormatException e) {
            session.setAttribute("error", "Formato de número inválido");
        } catch (Exception e) {
            session.setAttribute("error", e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/eu/investimentos");
    }
}
