package com.trabalho.devweb.infrastructure.controllers;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;
import java.io.IOException;

@WebServlet(name = "LogoutController", urlPatterns = "/sair")
public class LogoutController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie jwtCookie = new Cookie("jwt", "");
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(0);
        resp.addCookie(jwtCookie);

        resp.sendRedirect(req.getContextPath() + "/login?logout=true");
    }
}
