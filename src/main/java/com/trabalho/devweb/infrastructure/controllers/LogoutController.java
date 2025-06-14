package com.trabalho.devweb.infrastructure.controllers;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;
import java.io.IOException;

@WebServlet(name = "LogoutController", urlPatterns = "/sair")
public class LogoutController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie accessToken = new Cookie("access_token", "");
        accessToken.setPath("/");
        accessToken.setHttpOnly(true);
        accessToken.setMaxAge(0);

        Cookie refreshToken = new Cookie("refresh_token", "");
        refreshToken.setPath("/");
        refreshToken.setHttpOnly(true);
        refreshToken.setMaxAge(0);

        resp.addCookie(accessToken);
        resp.addCookie(refreshToken);

        req.getSession().setAttribute("success", "Você foi desconectado com sucesso.");
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
