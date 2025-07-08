<%@page import="com.mycompany.devbank.Transferencia"%>
<%@page import="com.mycompany.devbank.DestinatarioFrequente"%>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.List" %>
<%@page import="java.math.BigDecimal" %>

<%
    int pagina = request.getAttribute("pagina") != null ? (int) request.getAttribute("pagina") : 1;
    int totalPaginas = request.getAttribute("totalPaginas") != null ? (int) request.getAttribute("totalPaginas") : 1;
    String busca = request.getAttribute("busca") != null ? (String) request.getAttribute("busca") : "";
    String filtro = request.getAttribute("filtro") != null ? (String) request.getAttribute("filtro") : "todos";
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8" />
    <title>Transferências - DevBank</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/transferencias.css" />
    <style>
        .btn--secondary.active {
            background-color: var(--color-primary);
            color: var(--color-white);
            border-color: var(--color-primary);
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Transferências Recentes</h1>

    <div class="layout">
        <div class="sidebar">
            <div class="card transfer-summary">
                <div class="summary-item sent">
                    <span>Enviados</span>
                    <span>- R$ <%= request.getAttribute("totalEnviado") != null ? ((BigDecimal)request.getAttribute("totalEnviado")).setScale(2).toString().replace('.', ',') : "0,00" %></span>
                </div>
                <div class="summary-item received">
                    <span>Recebidos</span>
                    <span>+ R$ <%= request.getAttribute("totalRecebido") != null ? ((BigDecimal)request.getAttribute("totalRecebido")).setScale(2).toString().replace('.', ',') : "0,00" %></span>
                </div>
                <div class="summary-item net">
                    <span>Saldo</span>
                    <span>R$ <%= request.getAttribute("saldo") != null ? ((BigDecimal)request.getAttribute("saldo")).setScale(2).toString().replace('.', ',') : "0,00" %></span>
                </div>
            </div>

            <div class="card">
                <h2>Destinatários Frequentes</h2>
                <div class="frequents">
                    <%
                        List<DestinatarioFrequente> frequentes = (List<DestinatarioFrequente>) request.getAttribute("frequentes");
                        if (frequentes != null && !frequentes.isEmpty()) {
                            for (DestinatarioFrequente f : frequentes) {
                    %>
                    <div class="frequent-recipient">
                        <%= f.getNome() %> <br />
                        <%= f.getBancoFormatado() %>
                    </div>
                    <%
                            }
                        } else {
                    %>
                    <p>Nenhum destinatário frequente encontrado.</p>
                    <%
                        }
                    %>
                </div>
            </div>
        </div>

        <div class="main">
            <div class="filter-buttons">
                <form method="get" action="${pageContext.request.contextPath}/transferencias" style="display: flex; gap: 0.5rem; flex-wrap: wrap; align-items: center;">
                    <input type="text" name="busca" placeholder="Buscar por nome..." value="<%= busca %>" class="btn btn--secondary" style="flex-grow: 1;" />
                    <input type="hidden" name="pagina" value="1" />
                    <button type="submit" name="filtro" value="todos" class="btn btn--secondary <%= filtro.equals("todos") ? "active" : "" %>">Todos</button>
                    <button type="submit" name="filtro" value="enviados" class="btn btn--secondary <%= filtro.equals("enviados") ? "active" : "" %>">Enviados</button>
                    <button type="submit" name="filtro" value="recebidos" class="btn btn--secondary <%= filtro.equals("recebidos") ? "active" : "" %>">Recebidos</button>
                    <a href="nova-transferencia.jsp" class="btn btn--primary">Nova Transferência</a>
                </form>
            </div>

            <div class="transfer-list">
                <%
                    List<Transferencia> lista = (List<Transferencia>) request.getAttribute("transferencias");
                    if (lista != null && !lista.isEmpty()) {
                        for (Transferencia t : lista) {
                %>
                <div class="transfer-item <%= t.getValor().compareTo(new java.math.BigDecimal("0")) < 0 ? "negative" : "positive" %>">
                    <div class="info">
                        <strong><%= t.getValor().compareTo(new java.math.BigDecimal("0")) < 0 ? "Para: " : "De: " %><%= t.getDestinatarioNome() %></strong>
                        <span><%= t.getRemetenteNome() != null ? t.getRemetenteNome() : "" %></span>
                        <span class="status"><%= t.getStatus() %></span>
                    </div>
                    <div><%= (t.getValor().compareTo(new java.math.BigDecimal("0")) < 0 ? "-" : "+") + " R$ " + t.getValor().abs().setScale(2).toString().replace('.', ',') %></div>
                </div>
                <%
                        }
                    } else {
                %>
                <p>Nenhuma transferência encontrada.</p>
                <%
                    }
                %>
            </div>

            <div class="pagination" style="margin-top: 1rem; display: flex; gap: 0.5rem;">
                <form method="get" action="${pageContext.request.contextPath}/transferencias" style="display: flex; gap: 0.5rem;">
                    <input type="hidden" name="busca" value="<%= busca %>">
                    <input type="hidden" name="filtro" value="<%= filtro %>">
                    <% for (int i = 1; i <= totalPaginas; i++) { %>
                        <button type="submit" name="pagina" value="<%= i %>" class="btn btn--secondary <%= (i == pagina) ? "active" : "" %>">
                            <%= i %>
                        </button>
                    <% } %>
                </form>
            </div>
        </div>
    </div>
</div>

</body>
</html>
