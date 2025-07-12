<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.trabalho.devweb.domain.Transfer" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.math.BigDecimal" %>

<%
    List<Transfer> transfers = (List<Transfer>) request.getAttribute("transfers");
    List<Transfer> frequents = (List<Transfer>) request.getAttribute("frequents");

    int currentPage = (request.getAttribute("page") != null) ? (Integer) request.getAttribute("page") : 1;
    int totalPages = (request.getAttribute("totalPages") != null) ? (Integer) request.getAttribute("totalPages") : 1;

    if (transfers == null || transfers.isEmpty()) {
        totalPages = 0;
    }

    String filterType = (String) request.getAttribute("filterType");
    String nameFilter = (String) request.getAttribute("nameFilter");

    BigDecimal totalSent = (BigDecimal) request.getAttribute("totalSent");
    BigDecimal totalReceived = (BigDecimal) request.getAttribute("totalReceived");
    BigDecimal balance = (BigDecimal) request.getAttribute("balance");

    boolean isNegativeBalance = balance.compareTo(BigDecimal.ZERO) < 0;
    String balanceClass = isNegativeBalance ? "sent" : "received";
    String balancePrefix = isNegativeBalance ? "- R$ " : "+ R$ ";

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>

<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8" />
  <title>Transferências - DevBank</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/transfers.css">
</head>
<body>
  <%@ include file="components/header.jsp" %>

  <div class="container container2">
    <h1>Transferências Recentes</h1>
    <div class="layout">
      <div class="sidebar">
        <div class="card transfer-summary">
          <div class="balance">
            <h3>Saldo</h3>
            <span><%= balancePrefix + String.format("%.2f", balance.abs()) %></span>
          </div>
          <div class="summary-item sent">
            <span>Enviado</span>
            <span>- R$ <%= String.format("%.2f", totalSent) %></span>
          </div>
          <div class="summary-item received">
            <span>Recebido</span>
            <span>+ R$ <%= String.format("%.2f", totalReceived) %></span>
          </div>
        </div>

        <div class="card">
          <h2>Destinatários Frequentes</h2>
          <div class="frequents">
            <%
              if (frequents != null && !frequents.isEmpty()) {
                for (Transfer r : frequents) {
                  String number = r.getTargetAccountNumber();
                  String masked = number.length() >= 4 ? "****" + number.substring(number.length() - 4) : number;
            %>
              <div class="frequent-recipient">
                <%= r.getTargetName() %> <br />
                Conta - <%= masked %>
              </div>
            <%
                }
              } else {
            %>
              <p>Nenhum destinatário frequente ainda.</p>
            <%
              }
            %>
          </div>
        </div>
      </div>

      <div class="main">
        <form method="get" action="transferencias" class="filter-buttons">
          <input type="text" name="name" placeholder="Buscar por nome..." value="<%= nameFilter != null ? nameFilter : "" %>" class="btn btn--secondary" />
          <button type="submit" name="type" value="all" class="btn btn--secondary <%= (filterType == null || filterType.equals("all")) ? "btn--active" : "" %>">Todos</button>
          <button type="submit" name="type" value="sent" class="btn btn--secondary <%= "sent".equals(filterType) ? "btn--active" : "" %>">Enviados</button>
          <button type="submit" name="type" value="received" class="btn btn--secondary <%= "received".equals(filterType) ? "btn--active" : "" %>">Recebidos</button>
          <a href="${pageContext.request.contextPath}/transferir" class="btn btn--primary">Nova Transferência</a>
        </form>

        <div class="transfer-list">
          <%
            if (transfers != null && !transfers.isEmpty()) {
              for (Transfer t : transfers) {
                boolean isSent = t.getOriginId().equals(session.getAttribute("account") != null
                    ? ((com.trabalho.devweb.domain.Account)session.getAttribute("account")).getId()
                    : "");
                String cssClass = isSent ? "negative" : "positive";
                String label = isSent ? "Para" : "De";
                String number = t.getTargetAccountNumber();
                String masked = number.length() >= 4 ? "****" + number.substring(number.length() - 4) : number;
          %>
            <div class="transfer-item <%= cssClass %>">
              <div class="info">
                <strong><%= label %>: <%= t.getTargetName() %></strong><br />
                <span>Conta final <%= masked %></span><br />
                <span class="status">✔️ Concluída · <%= sdf.format(java.sql.Timestamp.valueOf(t.getCreatedAt())) %></span>
              </div>
              <div>
                <%= (isSent ? "- R$ " : "+ R$ ") + String.format("%.2f", t.getAmount()) %>
              </div>
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

        <% if (totalPages > 1) { %>
          <div class="pagination">
            <%
              String baseUrl = "transferencias?name=" + (nameFilter != null ? nameFilter : "") + "&type=" + (filterType != null ? filterType : "");

              int maxButtons = 5;
              int startPage = Math.max(1, currentPage - 2);
              int endPage = Math.min(totalPages, startPage + maxButtons - 1);
              if (endPage - startPage < maxButtons - 1) startPage = Math.max(1, endPage - maxButtons + 1);

              if (startPage > 1) {
            %>
              <a href="<%= baseUrl + "&page=1" %>" class="btn">1</a>
              <span class="dots">...</span>
            <%
              }

              for (int i = startPage; i <= endPage; i++) {
            %>
              <a href="<%= baseUrl + "&page=" + i %>" class="btn <%= (i == currentPage) ? "active" : "" %>"><%= i %></a>
            <%
              }

              if (endPage < totalPages) {
            %>
              <span class="dots">...</span>
              <a href="<%= baseUrl + "&page=" + totalPages %>" class="btn"><%= totalPages %></a>
            <%
              }
            %>
          </div>
        <% } %>
      </div>
    </div>
  </div>
</body>
</html>
