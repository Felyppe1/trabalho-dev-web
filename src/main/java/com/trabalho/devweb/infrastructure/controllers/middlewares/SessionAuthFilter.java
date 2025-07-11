package com.trabalho.devweb.infrastructure.controllers.middlewares;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import com.trabalho.devweb.domain.Account;
import com.trabalho.devweb.infrastructure.databaseconnection.PostgresConnection;
import com.trabalho.devweb.infrastructure.repositories.AccountsRepository;

@WebFilter(filterName = "SessionAuthFilter", urlPatterns = { "/home", "/extrato", "/transferir", "/transferencias", "/eu/investimentos", "/investimentos", "/statement/*", "/comprar-investimento/*", "/deposito", "/saque", "/alterar-senha", "/configuracoes"  })
public class SessionAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);

        Account account = null;

        if (session != null) {
            account = (Account) session.getAttribute("account");
        }

        if (account != null) {
            try (Connection connection = PostgresConnection.getConnection()) {
                AccountsRepository accountsRepository = new AccountsRepository(connection);
                Account updatedAccount = accountsRepository.findById(account.getId());

                if (updatedAccount != null) {
                    session.setAttribute("account", updatedAccount);
                    chain.doFilter(request, response);
                    return;
                }
                
            } catch (SQLException e) {
                // Do nothing
                
            }
        }

        HttpSession newSession = req.getSession(true);
        newSession.setAttribute("redirect", req.getRequestURI());
        newSession.setAttribute("error", "session-missing");

        resp.sendRedirect(req.getContextPath() + "/login");
        return;

    }
}
