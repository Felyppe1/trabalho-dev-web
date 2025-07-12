<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<div class="security-section">
    <h2>Configurações de Segurança</h2>
    <p class="section-description">Gerencie suas preferências de segurança da conta.</p>
    
    <div class="security-settings">        
            <form class="password-form" method="POST" action="${pageContext.request.contextPath}/alterar-senha">
                <div class="form-group">
                    <label for="currentPassword">Senha Atual</label>
                    <input type="password" id="currentPassword" name="currentPassword" 
                           placeholder="Digite sua senha atual" required />
                </div>
                
                <div class="form-group">
                    <label for="newPassword">Nova Senha</label>
                    <input type="password" id="newPassword" name="newPassword" 
                           placeholder="Digite sua nova senha" required 
                           minlength="6" />
                </div>
                
                <div class="form-group">
                    <label for="confirmPassword">Confirmar Nova Senha</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" 
                           placeholder="Confirme sua nova senha" required 
                           minlength="6" />
                </div>
                
                <button type="submit" class="button">Atualizar Senha</button>
            </form>
       
        
        
    </div>
</div>

<script>
    document.getElementById('confirmPassword').addEventListener('input', function() {
        const newPassword = document.getElementById('newPassword').value;
        const confirmPassword = this.value;
        
        if (newPassword !== confirmPassword) {
            this.setCustomValidity('As senhas não coincidem');
        } else {
            this.setCustomValidity('');
        }
    });
</script>
