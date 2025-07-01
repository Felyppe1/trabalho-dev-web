<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/transfer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/components/header.css">
    <title>DevBank</title>
</head>
<body>
    <%@ include file="components/header.jsp" %>
  
    <main class="page">
      <section class="card transfer">
        <h1 class="transfer__title">Transfer Money</h1>
        <p class="transfer__subtitle">Transfer money between your accounts or to someone else.</p>
  
        <form class="transfer__form">
          <label class="transfer__form-label">From Account</label>
          <select class="transfer__form-select">
            <option>Select account</option>
          </select>
  
          <label class="transfer__form-label">To Account</label>
          <select class="transfer__form-select">
            <option>Select account</option>
          </select>
  
          <label class="transfer__form-label">Amount</label>
          <input class="transfer__form-input" type="number" placeholder="$ 0.00">
  
          <label class="transfer__form-label">Description (Optional)</label>
          <input class="transfer__form-input" type="text" placeholder="What's this for?">
  
          <button class="transfer__form-button" type="submit">Transfer Money</button>
        </form>
      </section>
  
      <aside class="card recipients">
        <h2 class="recipients__title">Recent Recipients</h2>
  
        <div class="recipients__list">
            <div class="recipients__card">
                <span>John Doe</span>
                <span class="recipients__bank">Chase Bank - ****3456</span>
            </div>
            <div class="recipients__card">
                <span>Jane Smith</span>
                <span class="recipients__bank">Bank of America - ****5432</span>
            </div>
            <div class="recipients__card">
                <span>Robert Johnson</span>
                <span class="recipients__bank">Wells Fargo - ****3210</span>
            </div>
        </div>
      </aside>
    </main>
  </body>

</html>