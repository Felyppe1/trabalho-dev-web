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

import com.trabalho.devweb.application.LoginService;
import com.trabalho.devweb.infrastructure.repositories.AccountsRepository;
import com.trabalho.devweb.infrastructure.databaseconnection.PostgresConnection;
import com.trabalho.devweb.domain.Account;

import java.security.Key;
import java.io.IOException;
import java.util.Date;
import java.sql.Connection;

@WebServlet(name = "LoginController", urlPatterns = "/login")
public class LoginController extends HttpServlet {
    private static final Key SECRET_KEY = Keys
            .hmacShaKeyFor("sua-chave-super-secreta-que-tem-mais-de-32-caracteres".getBytes());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String error = (String) session.getAttribute("error");
        String success = (String) session.getAttribute("success");

        if ("jwt-invalid".equals(error)) {
            req.setAttribute("error", "Sessão inválida. Faça login novamente.");
        } else if ("jwt-missing".equals(error)) {
            req.setAttribute("error", "Você precisa estar logado para acessar essa página.");
        } else if (success != null) {
            req.setAttribute("success", success);
        }

        session.removeAttribute("error");
        session.removeAttribute("success");

        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try (Connection conn = PostgresConnection.getConnection()) {
            AccountsRepository repo = new AccountsRepository(conn);
            LoginService loginService = new LoginService(repo);
            Account account = loginService.authenticate(email, password);

            String accessToken = Jwts.builder()
                    .setSubject(account.getId())
                    .claim("email", account.getEmail())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 60 * 1000)) // 1 minuto
                    .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                    .compact();

            String refreshToken = Jwts.builder()
                    .setSubject(account.getId())
                    .claim("email", account.getEmail())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000)) // 7 dias
                    .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                    .compact();

            Cookie jwtCookie = new Cookie("access_token", accessToken);
            jwtCookie.setHttpOnly(true); // Proteger contra acesso via JavaScript
            jwtCookie.setMaxAge(60 * 60); // 1 hora
            jwtCookie.setPath("/");
            // jwtCookie.setSecure(true); // Só envia via HTTPS
            resp.addCookie(jwtCookie);

            Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 dias
            refreshCookie.setPath("/");
            resp.addCookie(refreshCookie);

            HttpSession session = req.getSession();
            String redirectUrl = (String) session.getAttribute("redirect");

            if (redirectUrl != null) {
                session.removeAttribute("redirect");
                resp.sendRedirect(redirectUrl);
            } else {
                resp.sendRedirect(req.getContextPath() + "/home");
            }
        } catch (Exception e) {
            req.setAttribute("error", "Usuário ou senha inválidos.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
