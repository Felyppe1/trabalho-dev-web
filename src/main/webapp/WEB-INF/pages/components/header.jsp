<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%
    String currentPath = request.getServletPath();
%>

<header class="header">
    <a href="${pageContext.request.contextPath}/home" class="header__logo">Dev<span>Bank</span></a>
    <nav class="header__nav">
        <a
            href="${pageContext.request.contextPath}/home"
            class="header__a <%= currentPath.contains("home.jsp") ? "header__a--active" : "" %>">
                Página Inicial
        </a>
        <a
            href="${pageContext.request.contextPath}/transferencias"
            class="header__a <%= currentPath.contains("transfer") ? "header__a--active" : "" %>">
                Transferências
        </a>
        <a
            href="${pageContext.request.contextPath}/eu/investimentos"
            class="header__a <%= currentPath.contains("investment") ? "header__a--active" : "" %>">
                Investimentos
        </a>
        <a
            href="${pageContext.request.contextPath}/extrato"
            class="header__a <%= currentPath.contains("statement") ? "header__a--active" : "" %>">
                Extrato
        </a>
        <a
            href="${pageContext.request.contextPath}/configuracoes"
            class="header__a <%= currentPath.contains("settings") ? "header__a--active" : "" %>">
                Configurações
        </a>
        <a
            href="${pageContext.request.contextPath}/sair"
            class="header__a">
                Sair
        </a>
    </nav>
</header>