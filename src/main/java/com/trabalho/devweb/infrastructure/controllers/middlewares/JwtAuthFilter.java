package com.trabalho.devweb.infrastructure.controllers.middlewares;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.security.Key;

@WebFilter(filterName = "JwtAuthFilter", urlPatterns = {"/home/*", "/extrato/*"})
public class JwtAuthFilter implements Filter {
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(
            "sua-chave-super-secreta-que-tem-mais-de-32-caracteres".getBytes());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession();

        String jwt = getJwtFromCookies(req);

        if (jwt != null) {
            try {
                Jws<Claims> claims = Jwts.parserBuilder()
                        .setSigningKey(SECRET_KEY)
                        .build()
                        .parseClaimsJws(jwt);

                req.setAttribute("user", claims.getBody().getSubject());
                chain.doFilter(request, response);
                return;
            } catch (JwtException e) {
                session.setAttribute("redirect", req.getRequestURI());
                session.setAttribute("error", "jwt-invalid");
                resp.sendRedirect(req.getContextPath() + "/login");
            }
        }

        session.setAttribute("redirect", req.getRequestURI());
        session.setAttribute("error", "jwt-missing");
        resp.sendRedirect(req.getContextPath() + "/login");
    }

    private String getJwtFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
