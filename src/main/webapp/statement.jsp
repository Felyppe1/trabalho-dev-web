<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>BankX - Statements</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/global.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/statement.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/components/header.css">
</head>
<body>
    <%@ include file="components/header.jsp" %>


  <main class="container">
    <section class="statements">
      <div class="statements__header">
        <h1 class="statements__title">Bank Statements</h1>
        <button class="btn btn--primary">
          <span class="btn__icon">📄</span> Request Statement
        </button>
      </div>

      <div class="statements__year-select">
        <select>
          <option selected>2025</option>
        </select>
      </div>

      <ul class="statements__list">
        <li class="statement">
          <div class="statement__info">
            <div class="statement__icon">📄</div>
            <div>
              <strong>April 2025 Statement</strong>
              <p>Generated on Apr 1, 2025 • 245 KB</p>
            </div>
          </div>
          <div class="statement__actions">
            <button class="btn btn--secondary">👁️ View</button>
            <button class="btn btn--secondary">⬇️ Download</button>
          </div>
        </li>
        <li class="statement">
          <div class="statement__info">
            <div class="statement__icon">📄</div>
            <div>
              <strong>March 2025 Statement</strong>
              <p>Generated on Mar 1, 2025 • 267 KB</p>
            </div>
          </div>
          <div class="statement__actions">
            <button class="btn btn--secondary">👁️ View</button>
            <button class="btn btn--secondary">⬇️ Download</button>
          </div>
        </li>
        <li class="statement">
          <div class="statement__info">
            <div class="statement__icon">📄</div>
            <div>
              <strong>February 2025 Statement</strong>
              <p>Generated on Feb 1, 2025 • 234 KB</p>
            </div>
          </div>
          <div class="statement__actions">
            <button class="btn btn--secondary">👁️ View</button>
            <button class="btn btn--secondary">⬇️ Download</button>
          </div>
        </li>
        <li class="statement">
          <div class="statement__info">
            <div class="statement__icon">📄</div>
            <div>
              <strong>January 2025 Statement</strong>
              <p>Generated on Jan 1, 2025 • 251 KB</p>
            </div>
          </div>
          <div class="statement__actions">
            <button class="btn btn--secondary">👁️ View</button>
            <button class="btn btn--secondary">⬇️ Download</button>
          </div>
        </li>
      </ul>
    </section>

    <section class="statement-settings">
      <h2 class="statement-settings__title">Statement Settings</h2>
      <div class="statement-settings__grid">
        <div>
          <h3 class="statement-settings__label">Delivery Preferences</h3>
          <p>Email Delivery<br><small>Receive statements via email</small></p>
          <button class="btn btn--secondary">Change</button>
        </div>
        <div>
          <h3 class="statement-settings__label">Account Selection</h3>
          <p>Premium Checking<br><small>****5678</small></p>
          <span class="badge badge--active">Active</span>
        </div>
      </div>
    </section>
  </main>
</body>
</html>
