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
    <% String error = (String) request.getAttribute("error"); %>
    
    <div class="notification-area">
    <% if (error != null) { %>
        <div class="notification notification--error"><%= error %></div>
    <% } %>
    </div>
    <script>
        document.querySelectorAll('.notification').forEach((el, i) => {
            setTimeout(() => {
                el.classList.add('notification--hide');
            }, 5000 + i * 300);
        });
    </script>

    <%@ include file="components/header.jsp" %>
    
    <%
        Investment investment = (Investment) request.getAttribute("investment");
        
        // Formatadores
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        
        // Calcular valores para exibição
        String investmentTitle = investment.getCategory() + " " + investment.getExpiration().toLocalDate().getYear();
        String formattedReturn = investment.getRentabilityIndex() != null && !investment.getRentabilityIndex().isEmpty() ?
            investment.getRentabilityIndex() + " + " + investment.getRentabilityPercent().stripTrailingZeros().toPlainString().replace('.', ',') + "%" :
            investment.getRentabilityPercent().stripTrailingZeros().toPlainString().replace('.', ',') + "%";
        
        // Calcular investimento mínimo (preço unitário / 100)
        double minimumInvestment = investment.getUnitPrice().doubleValue() / 100.0;
        
        // Criar ID do investimento no formato CATEGORY-YEAR (removendo "TESOURO " da category)
        String category = investment.getCategory().replace("TESOURO ", "");
        int year = investment.getExpiration().toLocalDate().getYear();
        String investmentId = category + "-" + year;
        
        // Determinar tipo do investimento para aplicar cores
        String investmentType = "";
        if (investment.getCategory().contains("PREFIXADO")) {
            investmentType = "prefixado";
        } else if (investment.getCategory().contains("SELIC")) {
            investmentType = "selic";
        } else if (investment.getCategory().contains("IPCA")) {
            investmentType = "ipca";
        } else if (investment.getCategory().contains("RENDA")) {
            investmentType = "renda";
        } else if (investment.getCategory().contains("EDUCA")) {
            investmentType = "educa";
        }
    %>
    
    <main class="main">
        <div class="page-header">
            <a href="${pageContext.request.contextPath}/investimentos" class="back-link">
                ← Voltar para Investimentos
            </a>
        </div>

        <div class="page-title-section">
            <h1>Comprar Investimento</h1>
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
                    <span class="card-title-icon card-title-icon--<%= investmentType %>"></span>
                    Investimento Selecionado
                </h2>

                <div class="investment-name investment-name--<%= investmentType %>"><%= investmentTitle %></div>

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
                        <span class="detail-value"><%= currencyFormat.format(minimumInvestment) %></span>
                    </div>
                    <div class="detail-item">
                        <span class="detail-label">Vencimento</span>
                        <span class="detail-value"><%= dateFormat.format(investment.getExpiration()) %></span>
                    </div>
                </div>

                <div class="info-box info-box--<%= investmentType %>">
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
                    Checkout
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
                        <div class="form-hint">Mínimo: <%= currencyFormat.format(minimumInvestment) %></div>
                    </div>

                    <!-- Investment Summary -->
                    <div id="investmentSummary" class="investment-summary" style="display: none;">
                        <h3 class="summary-title">Resumo do Investimento</h3>
                        <div class="summary-item">
                            <span>Frações de investimento:</span>
                            <span id="fractions" class="summary-value">0</span>
                        </div>
                        <div class="summary-item">
                            <span>Custo total:</span>
                            <span id="totalCost" class="summary-value">R$ 0,00</span>
                        </div>
                        <div class="summary-item">
                            <span>Valor a ser estornado:</span>
                            <span id="remainingAmount" class="summary-value">R$ 0,00</span>
                        </div>
                    </div>

                    <button type="submit" class="button button-confirm">
                        Confirmar Compra
                    </button>
                </form>
            </div>
        </div>
    </main>

    <script>
        // Investment minimum amount
        const minimumAmount = <%= minimumInvestment %>;
        const minimumAmountFormatted = '<%= currencyFormat.format(minimumInvestment) %>';
        
        // Format currency input and calculate summary
        document.getElementById('amount').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, '');
            value = (value / 100).toLocaleString('pt-BR', {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
            });
            e.target.value = value;
            
            // Calculate and update investment summary
            updateInvestmentSummary(value);
        });

        function updateInvestmentSummary(formattedAmount) {
            const summaryDiv = document.getElementById('investmentSummary');
            
            if (!formattedAmount || formattedAmount === '0,00') {
                summaryDiv.style.display = 'none';
                return;
            }
            
            const numericAmount = parseFloat(formattedAmount.replace('.', '').replace(',', '.'));
            
            if (numericAmount > 0) {
                const fractions = Math.floor(numericAmount / minimumAmount);
                
                const totalCost = fractions * minimumAmount;
                
                const remainingAmount = numericAmount - totalCost;
                
                document.getElementById('fractions').textContent = fractions;
                document.getElementById('totalCost').textContent = totalCost.toLocaleString('pt-BR', {
                    style: 'currency',
                    currency: 'BRL'
                });
                document.getElementById('remainingAmount').textContent = remainingAmount.toLocaleString('pt-BR', {
                    style: 'currency',
                    currency: 'BRL'
                });
                
                summaryDiv.style.display = 'block';
            } else {
                summaryDiv.style.display = 'none';
            }
        }
    </script>
</body>
</html>
