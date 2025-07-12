<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="com.trabalho.devweb.domain.Account" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/settings.css">
 
    <title>DevBank</title>
</head>
<body>
    <%
        Account account = (Account) session.getAttribute("account");
    %>
    
    <%@ include file="../components/header.jsp" %>

    <% String successMessage = (String) request.getAttribute("successMessage"); %>
    <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
                

    <div class="notification-area">
    <% if (successMessage != null) { %>
        <div class="notification notification--success"><%= successMessage %></div>
    <% } %>
    <% if (errorMessage != null) { %>
        <div class="notification notification--error"><%= errorMessage %></div>
    <% } %>
    </div>
    <script>
        document.querySelectorAll('.notification').forEach((el, i) => {
            setTimeout(() => {
                el.classList.add('notification--hide');
            }, 5000 + i * 300);
        });
    </script>
    
    <main class="page settings-page">
        <div class="settings-header">
            <h1>Configurações</h1>
        </div>
        
        <div class="settings-tabs">
            <div class="tab-item <%= request.getParameter("tab") == null || "profile".equals(request.getParameter("tab")) ? "active" : "" %>">
                <a href="?tab=profile">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M20 21V19C20 17.9391 19.5786 16.9217 18.8284 16.1716C18.0783 15.4214 17.0609 15 16 15H8C6.93913 15 5.92172 15.4214 5.17157 16.1716C4.42143 16.9217 4 17.9391 4 19V21" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        <circle cx="12" cy="7" r="4" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                    Perfil
                </a>
            </div>
            <div class="tab-item <%= "security".equals(request.getParameter("tab")) ? "active" : "" %>">
                <a href="?tab=security">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M12 22S8 18 8 14V8C8 5.79086 9.79086 4 12 4S16 5.79086 16 8V14C16 18 12 22 12 22Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        <path d="M10 8C10 6.89543 10.8954 6 12 6C13.1046 6 14 6.89543 14 8" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                    Segurança
                </a>
            </div>
        </div>

        <div class="settings-content">
            <% 
                String activeTab = request.getParameter("tab");
                if (activeTab == null) {
                    activeTab = (String) request.getAttribute("tab");
                }
                if (activeTab == null) {
                    activeTab = "profile";
                }
            %>
            
            <% if ("profile".equals(activeTab)) { %>
                <%@ include file="profile.jsp" %>
            <% } else if ("security".equals(activeTab)) { %>
                <%@ include file="security.jsp" %>
            <% } %>
        </div>
    </main>
  </body>

</html>