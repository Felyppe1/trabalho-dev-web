<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<div class="profile-section">
    <h2>Informações Pessoais</h2>
    <p class="section-description">Atualize os detalhes da sua conta.</p>
    
    <form class="profile-form" method="POST" action="${pageContext.request.contextPath}/configuracoes">
        <div class="form-row">
            <div class="form-group">
                <label for="firstName">Primeiro Nome</label>
                <%
                    String[] nameParts = account.getName().split(" ");
                    String firstName = nameParts.length > 0 ? nameParts[0] : "";
                %>
                <input type="text" id="firstName" name="firstName" 
                       value="<%= firstName %>" required />
            </div>
            <div class="form-group">
                <label for="lastName">Último Nome</label>
                <%
                    String lastName = nameParts.length > 1 ? nameParts[nameParts.length - 1] : "";
                %>
                <input type="text" id="lastName" name="lastName" 
                       value="<%= lastName %>" />
            </div>
        </div>
        
        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" 
                   value="<%= account.getEmail() %>" required />
        </div>
        
        <div class="form-group">
            <label for="phone">Telefone</label>
            <input type="tel" id="phone" name="phone" 
                   value="<%= account.getCellphoneNumber() %>" required />
        </div>
        
        <div class="form-group">
            <label for="address">Endereço</label>
            <input type="text" id="address" name="address" 
                   value="<%= account.getAddress() %>" required />
        </div>
        
        <button type="submit" class="button">Salvar Alterações</button>
    </form>
</div>
