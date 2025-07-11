package com.trabalho.devweb.infrastructure.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.trabalho.devweb.application.LoginService;
import com.trabalho.devweb.infrastructure.repositories.AccountsRepository;
import com.trabalho.devweb.infrastructure.databaseconnection.PostgresConnection;
import com.trabalho.devweb.domain.Account;

import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "LoginController", urlPatterns = "/login")
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String error = (String) session.getAttribute("error");
        String success = (String) session.getAttribute("success");

        if ("session-invalid".equals(error)) {
            req.setAttribute("error", "Sessão inválida. Faça login novamente.");
        } else if ("session-missing".equals(error)) {
            req.setAttribute("error", "Você precisa estar logado para acessar essa página.");
        } else if (success != null) {
            req.setAttribute("success", success);
        }

        session.removeAttribute("error");
        session.removeAttribute("success");

        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try (Connection conn = PostgresConnection.getConnection()) {
            AccountsRepository accountsRepository = new AccountsRepository(conn);

            LoginService loginService = new LoginService(accountsRepository);

            Account account = loginService.execute(email, password);

            HttpSession session = req.getSession(true);
            session.setAttribute("account", account);

            session.setMaxInactiveInterval(10 * 60);

            String redirectUrl = (String) session.getAttribute("redirect");

            if (redirectUrl != null) {
                session.removeAttribute("redirect");
                resp.sendRedirect(redirectUrl);
            } else {
                resp.sendRedirect(req.getContextPath() + "/home");
            }
        } catch (Exception e) {
            req.setAttribute("error", "Usuário ou senha inválidos.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
