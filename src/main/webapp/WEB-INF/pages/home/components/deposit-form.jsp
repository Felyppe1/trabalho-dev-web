<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<div class="deposit-form-section">
    <h2>
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 5V19M5 12L12 5L19 12" stroke="#0ea168" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        Realizar Depósito
    </h2>
    <p class="section-description">Adicione dinheiro à sua conta</p>
    
    <form class="deposit-form" method="POST" action="${pageContext.request.contextPath}/deposito">
        <div class="form-row">
            <div class="form-group">
                <label for="amount">Valor do Depósito *</label>
                <input type="number" id="amount" name="amount" 
                       placeholder="R$ 0,00" min="0.01" step="0.01" required />
            </div>
            <div class="form-group">
                <label for="description">Descrição (opcional)</label>
                <input type="text" id="description" name="description" 
                       placeholder="Ex: Depósito salário" maxlength="100" />
            </div>
        </div>
        
        <button type="submit" class="confirm-btn">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 5V19M5 12L12 5L19 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            Confirmar Depósito
        </button>
    </form>
</div>


