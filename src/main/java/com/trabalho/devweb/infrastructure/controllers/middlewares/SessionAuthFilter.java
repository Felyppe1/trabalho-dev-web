package com.trabalho.devweb.infrastructure.controllers.middlewares;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import com.trabalho.devweb.domain.Account;

@WebFilter(filterName = "SessionAuthFilter", urlPatterns = { "/home/*", "/extrato/*", "/transfer/*", "/statement/*" })
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
            chain.doFilter(request, response);
            return;
        }

        HttpSession newSession = req.getSession(true);
        newSession.setAttribute("redirect", req.getRequestURI());
        newSession.setAttribute("error", "session-missing");

        resp.sendRedirect(req.getContextPath() + "/login");
        return;

    }
}
