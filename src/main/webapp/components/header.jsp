<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%
    String currentPath = request.getServletPath();
%>

<header class="header">
    <div class="header__logo">Dev<span>Bank</span></div>
    <nav class="header__nav">
        <a
            href="${pageContext.request.contextPath}/home"
            class="header__a <%= currentPath.endsWith("/home") ? "header__a--active" : "" %>">
                Página Inicial
        </a>
        <a
            href="${pageContext.request.contextPath}/transferencia"
            class="header__a <%= currentPath.endsWith("/transferencia") ? "header__a--active" : "" %>">
                Transferência
        </a>
        <a
            href="${pageContext.request.contextPath}/extrato"
            class="header__a <%= currentPath.endsWith("/extrato") ? "header__a--active" : "" %>">
                Extrato
        </a>
        <a
            href="#"
            class="header__a <%= currentPath.endsWith("/settings") ? "active" : "" %>">
                Settings
        </a>
        <a
            href="${pageContext.request.contextPath}/sair"
            class="header__a">
                Sair
        </a>
    </nav>
</header>