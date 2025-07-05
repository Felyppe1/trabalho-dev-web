package com.trabalho.devweb.infrastructure.controllers;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;
import java.io.IOException;

@WebServlet(name = "LogoutController", urlPatterns = "/sair")
public class LogoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        HttpSession newSession = req.getSession(true);
        newSession.setAttribute("success", "Você foi desconectado com sucesso.");

        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
