<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.trabalho.devweb.domain.Transaction" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.TextStyle" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.time.ZonedDateTime" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="java.time.format.DateTimeFormatter" %>




<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>DevBank - Statements</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/global.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/statement.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/components/header.css">
</head>
<body>
    <%@ include file="components/header.jsp" %>
    <%
      ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
      String generatedTime = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy • HH:mm"));
    %>
  <main class="container">
    <section class="statements">
      <div class="statements__header">
        <h1 class="statements__title">Account Statement</h1>        
      </div>

      <div class="statements__year-select">
        <form method="get" action="extrato">
        <select name="year" onchange="this.form.submit()">
          <option value="2023" <%= "2023".equals(request.getParameter("year")) ? "selected" : "" %>>2023</option>
          <option value="2024" <%= "2024".equals(request.getParameter("year")) ? "selected" : "" %>>2024</option>
          <option value="2025" <%= "2025".equals(request.getParameter("year")) || request.getParameter("year") == null ? "selected" : "" %>>2025</option>
        </select>
  </form>
      </div>

      <ul class="statements__list">           
        <%
          String yearParam = request.getParameter("year");
          int selectedYear = (yearParam != null) ? Integer.parseInt(yearParam) : java.time.Year.now().getValue();

          java.time.YearMonth currentMonth = java.time.YearMonth.now();
          int lastMonth = currentMonth.getYear() == selectedYear ? currentMonth.getMonthValue() : 12;

          for (int i = 1; i <= lastMonth; i++) {
            java.time.YearMonth monthYear = java.time.YearMonth.of(selectedYear, i);
            String month = String.format("%02d", i);
        %>
        <li class="statement">
          <div class="statement__info">
            <div class="statement__icon">📄</div>
            <div>
              <strong> <%= monthYear.getMonth() %>/<%= monthYear.getYear() %> Extrato</strong>
              <p>Atualizado em <%= generatedTime %> </p>
            </div>
          </div>
          <div class="statement__actions">
            <form action="extrato/view" method="get">
                <input type="hidden" name="month" value="<%= month %>">
                <input type="hidden" name="year" value="<%= monthYear.getYear() %>">
              <button class="btn btn--secondary">Visualizar</button>
            </form>
            <form action="extrato/download" method="get">
              <input type="hidden" name="month" value="<%= month %>">
              <input type="hidden" name="year" value="<%= monthYear.getYear() %>">
              <button class="btn btn--secondary">Download</button>
            </form>
          </div>
        <%
          }
        %>                    
      </ul>
    </section>

    
  </main>
</body>
</html>
