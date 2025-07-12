package com.trabalho.devweb.infrastructure.controllers;

import com.trabalho.devweb.domain.Account;
import com.trabalho.devweb.domain.Transfer;
import com.trabalho.devweb.infrastructure.databaseconnection.PostgresConnection;
import com.trabalho.devweb.infrastructure.repositories.TransfersRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

@WebServlet(name = "TransfersController", urlPatterns = "/transferencias")
public class TransfersController extends HttpServlet {
    private static final int PAGE_SIZE = 5;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            session.setAttribute("error", "session-missing");
            resp.sendRedirect("login");
            return;
        }

        int page = Integer.parseInt(req.getParameter("page") != null ? req.getParameter("page") : "1");
        String direction = req.getParameter("type") != null ? req.getParameter("type") : "all";
        String nameFilter = req.getParameter("name") != null ? req.getParameter("name").toLowerCase() : "";

        try (Connection conn = PostgresConnection.getConnection()) {
            TransfersRepository repo = new TransfersRepository(conn);

            int offset = (page - 1) * PAGE_SIZE;

            List<Transfer> transfers = repo.findTransfersByAccount(account.getId(), offset, PAGE_SIZE, direction,
                    nameFilter);
            int total = repo.countTransfersByAccount(account.getId(), direction, nameFilter);
            int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);

            BigDecimal totalSent = repo.getTotalSent(account.getId());
            BigDecimal totalReceived = repo.getTotalReceived(account.getId());
            BigDecimal balance = account.getBalance(); // ✅ Agora o saldo é o valor correto do banco

            List<Transfer> frequents = repo.findTopFrequentRecipients(account.getId(), 3);

            req.setAttribute("transfers", transfers);
            req.setAttribute("frequents", frequents);
            req.setAttribute("totalSent", totalSent);
            req.setAttribute("totalReceived", totalReceived);
            req.setAttribute("balance", balance);
            req.setAttribute("page", page);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("filterType", direction);
            req.setAttribute("nameFilter", nameFilter);

            req.getRequestDispatcher("/WEB-INF/pages/transfers.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Erro ao carregar transferências.");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
        }
    }
}
