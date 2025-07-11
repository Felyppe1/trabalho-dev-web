<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<section class="card deposit-section">
    <div class="deposit-tabs">
        <a href="?action=deposit"  class="deposit-tab <%= request.getParameter("action") == null || "deposit".equals(request.getParameter("action")) ? "active" : "" %>">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 5V19M5 12L12 5L19 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            Depósito
        </a>
        <a href="?action=withdraw" class="deposit-tab <%= "withdraw".equals(request.getParameter("action")) ? "active" : "" %>">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 19V5M19 12L12 19L5 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            Saque
        </a>
    </div>

    <div class="deposit-content">
        <% 
            String activeAction = request.getParameter("action");
            if (activeAction == null) {
                activeAction = (String) request.getAttribute("action");
            }
            if (activeAction == null) {
                activeAction = "deposit";
            }
        %>
        
        <% if ("deposit".equals(activeAction)) { %>
            <%@ include file="deposit-form.jsp" %>
        <% } else if ("withdraw".equals(activeAction)) { %>
            <%@ include file="withdraw-form.jsp" %>
        <% } %>
    </div>
</section>
