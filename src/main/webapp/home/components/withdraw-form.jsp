<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<div class="withdraw-form-section">
    <h2>
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 19V5M19 12L12 19L5 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        Realizar Saque
    </h2>
    <p class="section-description">Retire dinheiro da sua conta</p>
    
    <form class="withdraw-form" method="POST" action="${pageContext.request.contextPath}/saque">
        <div class="form-row">
            <div class="form-group">
                <label for="withdrawAmount">Valor do Saque *</label>
                <input type="number" id="withdrawAmount" name="amount" 
                       placeholder="R$ 0,00" min="0.01" step="0.01" 
                       max="<%= account.getBalance() %>" required />
            </div>
            
            <div class="form-group">
                <label for="description">Descrição (opcional)</label> 
                <input type="text" id="description" name="description" 
                       placeholder="Ex: Pagamento de conta" maxlength="100" />
            </div>
        </div>
        
        <div class="withdraw-info">
            <h3>Informações sobre Saques</h3>
            <ul>
                <li>• Limite diário: R$ 5.000,00</li>                
            </ul>
        </div>
        
        <button type="submit" class="confirm-btn withdraw-btn">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 19V5M19 12L12 19L5 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            Confirmar Saque
        </button>
    </form>
</div>

<script>
    // Validação de saldo
    document.getElementById('withdrawAmount').addEventListener('input', function() {
        const maxAmount = parseFloat('<%= account.getBalance() %>');
        const enteredAmount = parseFloat(this.value);
        
        if (enteredAmount > maxAmount) {
            this.setCustomValidity('Valor não pode ser maior que o saldo disponível');
        } else {
            this.setCustomValidity('');
        }
    });
</script>
