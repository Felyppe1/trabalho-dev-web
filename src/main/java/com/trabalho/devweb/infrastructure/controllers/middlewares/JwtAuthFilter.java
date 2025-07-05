package com.trabalho.devweb.infrastructure.controllers.middlewares;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.security.Key;

@WebFilter(filterName = "JwtAuthFilter", urlPatterns = { "/home/*", "/extrato/*" })
public class JwtAuthFilter implements Filter {
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(
            "sua-chave-super-secreta-que-tem-mais-de-32-caracteres".getBytes());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession();

        // String jwt = getJwtFromCookies(req);
        String accessToken = getCookie("access_token", req);

        if (accessToken != null) {
            try {
                Jws<Claims> claims = Jwts.parserBuilder()
                        .setSigningKey(SECRET_KEY)
                        .build()
                        .parseClaimsJws(accessToken);

                req.setAttribute("accountId", claims.getBody().getSubject());
                chain.doFilter(request, response);
                return;
            } catch (ExpiredJwtException eje) {

                String refreshToken = getCookie("refresh_token", req);
                if (refreshToken != null) {
                    try {
                        Jws<Claims> refreshClaims = Jwts.parserBuilder()
                                .setSigningKey(SECRET_KEY)
                                .build()
                                .parseClaimsJws(refreshToken);

                        // Refresh token válido: gerar novo JWT
                        String accountId = refreshClaims.getBody().getSubject();

                        String newAccessToken = Jwts.builder()
                                .setSubject(accountId)
                                .setIssuedAt(new java.util.Date())
                                .setExpiration(new java.util.Date(System.currentTimeMillis() + 60 * 60 * 1000))
                                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                                .compact();

                        Cookie jwtCookie = new Cookie("access_token", newAccessToken);
                        jwtCookie.setHttpOnly(true);
                        jwtCookie.setMaxAge(60 * 60); // 1 hora
                        jwtCookie.setPath("/");
                        resp.addCookie(jwtCookie);

                        req.setAttribute("accountId", accountId);
                        chain.doFilter(request, response);

                        return;
                    } catch (JwtException e2) {
                        // Refresh token inválido
                    }
                }
                // Se refresh token não existe ou inválido, segue fluxo normal
                session.setAttribute("redirect", req.getRequestURI());
                session.setAttribute("error", "jwt-invalid");
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            } catch (JwtException e) {
                session.setAttribute("redirect", req.getRequestURI());
                session.setAttribute("error", "jwt-invalid");
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
        }

        session.setAttribute("redirect", req.getRequestURI());
        session.setAttribute("error", "jwt-missing");
        resp.sendRedirect(req.getContextPath() + "/login");
    }

    private String getCookie(String keyName, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (keyName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
