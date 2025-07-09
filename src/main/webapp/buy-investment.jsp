<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.trabalho.devweb.domain.Investment" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Comprar Investimento - Digital Bank</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/components/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/buy-investment.css">
</head>
<body>
    <%@ include file="components/header.jsp" %>
    
    <%
        Investment investment = (Investment) request.getAttribute("investment");
        
        // Formatadores
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        
        // Se não há investimento, mostrar erro
        if (investment == null) {
    %>
    <main class="main">
        <div class="error-message">
            <p>Investimento não encontrado.</p>
            <a href="investimentos" class="button">Voltar para Investimentos</a>
        </div>
    </main>
    <%
            return;
        }
        
        // Calcular valores para exibição
        String investmentTitle = investment.getCategory() + " " + investment.getExpiration().toLocalDate().getYear();
        String formattedReturn = investment.getRentabilityIndex() != null && !investment.getRentabilityIndex().isEmpty() ?
            investment.getRentabilityIndex() + " + " + investment.getRentabilityPercent().stripTrailingZeros().toPlainString().replace('.', ',') + "%" :
            investment.getRentabilityPercent().stripTrailingZeros().toPlainString().replace('.', ',') + "%";
        
        // Criar ID do investimento no formato CATEGORY-YEAR (removendo "TESOURO " da category)
        String category = investment.getCategory().replace("TESOURO ", "");
        int year = investment.getExpiration().toLocalDate().getYear();
        String investmentId = category + "-" + year;
    %>
    
    <main class="main">
        <div class="page-header">
            <a href="investimentos" class="back-link">
                ← Voltar para Investimentos
            </a>
        </div>

        <div class="page-title-section">
            <h1 class="page-title">Comprar Investimento</h1>
            <p class="page-subtitle">Adquirir títulos do tesouro e produtos de investimento</p>
        </div>

        <!-- Error/Success Messages -->
        <% String error = (String) request.getAttribute("error"); %>
        <% if (error != null) { %>
            <div class="error-message">
                <%= error %>
            </div>
        <% } %>

        <% String successMessage = (String) session.getAttribute("successMessage"); %>
        <% if (successMessage != null) { %>
            <div class="success-message">
                <%= successMessage %>
            </div>
            <% session.removeAttribute("successMessage"); %>
        <% } %>

        <div class="content-grid">
            <!-- Selected Investment Card -->
            <div class="investment-card">
                <h2 class="card-title">
                    <span class="card-title-icon"></span>
                    Investimento Selecionado
                </h2>

                <div class="investment-name"><%= investmentTitle %></div>

                <div class="investment-details">
                    <div class="detail-item">
                        <span class="detail-label">Retorno Anual</span>
                        <span class="detail-value annual-return"><%= formattedReturn %></span>
                    </div>
                    <div class="detail-item">
                        <span class="detail-label">Preço Unitário</span>
                        <span class="detail-value"><%= currencyFormat.format(investment.getUnitPrice()) %></span>
                    </div>
                    <div class="detail-item">
                        <span class="detail-label">Invest. Mínimo</span>
                        <span class="detail-value">R$ 6,99</span>
                    </div>
                    <div class="detail-item">
                        <span class="detail-label">Vencimento</span>
                        <span class="detail-value"><%= dateFormat.format(investment.getExpiration()) %></span>
                    </div>
                </div>

                <div class="info-box">
                    <span class="info-icon">ℹ</span>
                    <div class="info-text">
                        <strong>Informações do Investimento</strong><br>
                        <%= investment.getCategory().contains("PREFIXADO") ? "Este é um título do tesouro com taxa pré-fixada e retornos garantidos." :
                            investment.getCategory().contains("SELIC") ? "Este título acompanha a taxa SELIC e oferece rentabilidade pós-fixada." :
                            investment.getCategory().contains("IPCA") ? "Este título protege contra a inflação, acompanhando o IPCA." :
                            investment.getCategory().contains("EDUCA") ? "Título específico para educação com benefícios fiscais." :
                            "Produto de investimento com garantia do governo." %>
                    </div>
                </div>
            </div>

            <!-- Investment Calculator Card -->
            <div class="calculator-card">
                <h2 class="card-title">
                    <span class="calculator-icon">🧮</span>
                    Investment Calculator
                </h2>

                <form id="investmentForm" method="post" action="<%= request.getContextPath() %>/investimentos/<%= investmentId %>">
                    <div class="form-group">
                        <label class="form-label" for="amount">Valor do Investimento (R$)</label>
                        <input 
                            type="text" 
                            id="amount" 
                            name="amount" 
                            class="form-input" 
                            placeholder="0,00"
                            required
                        >
                        <div class="form-hint">Mínimo: R$ 6,99</div>
                    </div>

                    <button type="button" class="btn btn-secondary" onclick="simulateInvestment()">
                        <span class="btn-icon">📊</span>
                        Simular Investimento
                    </button>

                    <button type="submit" class="btn btn-primary">
                        <span class="btn-icon">📈</span>
                        Confirmar Compra
                    </button>
                </form>
            </div>
        </div>
    </main>

    <script>
        // Format currency input
        document.getElementById('amount').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, '');
            value = (value / 100).toLocaleString('pt-BR', {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
            });
            e.target.value = value;
        });

        // Simulate investment function
        function simulateInvestment() {
            const amount = document.getElementById('amount').value;
            if (!amount || parseFloat(amount.replace(',', '.')) < 6.99) {
                alert('Please enter a valid amount (minimum R$ 6,99)');
                return;
            }
            
            const numericAmount = parseFloat(amount.replace('.', '').replace(',', '.'));
            const annualReturn = 0.1398; // 13.98%
            const maturityDate = new Date('2028-01-01');
            const today = new Date();
            const yearsToMaturity = (maturityDate - today) / (365.25 * 24 * 60 * 60 * 1000);
            
            const finalAmount = numericAmount * Math.pow(1 + annualReturn, yearsToMaturity);
            const profit = finalAmount - numericAmount;
            
            alert(`Investment Simulation:\n\nInitial Amount: R$ ${numericAmount.toLocaleString('pt-BR', {minimumFractionDigits: 2})}\nFinal Amount: R$ ${finalAmount.toLocaleString('pt-BR', {minimumFractionDigits: 2})}\nProfit: R$ ${profit.toLocaleString('pt-BR', {minimumFractionDigits: 2})}\n\nThis is an estimate based on the current annual return rate.`);
        }

        // Form validation
        document.getElementById('investmentForm').addEventListener('submit', function(e) {
            const amount = document.getElementById('amount').value;
            const numericAmount = parseFloat(amount.replace('.', '').replace(',', '.'));
            
            if (!amount || numericAmount < 6.99) {
                e.preventDefault();
                alert('Por favor, insira um valor válido (mínimo R$ 6,99)');
                return;
            }
        });
    </script>
</body>
</html>
