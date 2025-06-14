package com.trabalho.devweb.infrastructure.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.io.IOException;
import java.util.Date;

// @WebServlet(name = "LoginController", urlPatterns = { "/login" })
public class LoginController extends HttpServlet {
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor("sua-chave-super-secreta-que-tem-mais-de-32-caracteres".getBytes());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String error = (String) session.getAttribute("error");
        String logout = req.getParameter("logout");

        if ("jwt-invalid".equals(error)) {
            req.setAttribute("error", "Sessão inválida. Faça login novamente.");
        } else if ("jwt-missing".equals(error)) {
            req.setAttribute("error", "Você precisa estar logado para acessar essa página.");
        } else if (logout != null) {
            req.setAttribute("success", "Você foi desconectado com sucesso.");
        }

        session.removeAttribute("error");

        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // TODO: implementar autenticação real com banco de dados
        if ("admin".equals(username) && "1234".equals(password)) {
            String jwt = Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 hora
                    .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                    .compact();
            
            Cookie jwtCookie = new Cookie("jwt", jwt);
            jwtCookie.setHttpOnly(true); // Proteger contra acesso via JavaScript
            jwtCookie.setMaxAge(60 * 60); // 1 hora
            jwtCookie.setPath("/");
            // jwtCookie.setSecure(true); // Só envia via HTTPS
            resp.addCookie(jwtCookie);
            
            HttpSession session = req.getSession();
            String redirectUrl = (String) session.getAttribute("redirect");

            if (redirectUrl != null) {
                session.removeAttribute("redirect");
                resp.sendRedirect(redirectUrl);
            } else {
                resp.sendRedirect(req.getContextPath() + "/home");
            }
        } else {
            req.setAttribute("error", "Usuário ou senha inválidos.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
