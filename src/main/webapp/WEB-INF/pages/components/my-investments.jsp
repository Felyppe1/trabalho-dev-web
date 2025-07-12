<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.trabalho.devweb.domain.MyInvestment" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    List<MyInvestment> myInvestments = (List<MyInvestment>) request.getAttribute("myInvestments");
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
%>

<div class="card">
    <div class="portfolio">
        <h2>Minha Carteira de Investimentos</h2>
        <p class="portfolio__subtitle">Seus investimentos atuais e seu desempenho</p>
    </div>

    <div class="investments-table">
        <div class="investments-table__header">
            <div>Título</div>
            <div>Retorno Anual</div>
            <div>Valor Investido</div>
            <div>Valor Atual</div>
            <div>Vencimento</div>
            <div></div>
        </div>

        <%
            if (myInvestments != null && !myInvestments.isEmpty()) {
                for (MyInvestment investment : myInvestments) {
                    String indicatorClass = "";
                    String nameClass = "";
                    
                    if (investment.getCategory().contains("PREFIXADO")) {
                        indicatorClass = "investment__indicator--prefixado";
                        nameClass = "investment__name--prefixado";
                    } else if (investment.getCategory().contains("SELIC")) {
                        indicatorClass = "investment__indicator--selic";
                        nameClass = "investment__name--selic";
                    } else if (investment.getCategory().contains("IPCA")) {
                        indicatorClass = "investment__indicator--ipca";
                        nameClass = "investment__name--ipca";
                    } else if (investment.getCategory().contains("EDUCA")) {
                        indicatorClass = "investment__indicator--educa";
                        nameClass = "investment__name--educa";
                    } else if (investment.getCategory().contains("RENDA")) {
                        indicatorClass = "investment__indicator--renda";
                        nameClass = "investment__name--renda";
                    }
        %>
            <div class="investments-table__row">
                <div class="investment">
                    <div class="investment__indicator <%= indicatorClass %>"></div>
                    <span class="investment__name <%= nameClass %>"><%= investment.getInvestmentTitle() %></span>
                </div>
                <div class="investment__return"><%= investment.getFormattedReturn() %></div>
                <div class="investment__amount"><%= currencyFormat.format(investment.getAmountInvested()) %></div>
                <div class="investment__value"><%= currencyFormat.format(investment.getCurrentValue()) %></div>
                <div class="investment__maturity"><%= dateFormat.format(investment.getMaturityDate()) %></div>
                <button 
                    onclick="openRedeemModal(
                        '<%= investment.getCategory() %>',
                        '<%= investment.getMaturityDate().toLocalDate().getYear() %>',
                        '<%= investment.getInvestmentTitle() %>',
                        '<%= investment.getAmountInvested() %>',
                        '<%= investment.getCurrentValue() %>',
                        '<%= investment.getFormattedReturn() %>',
                        '<%= dateFormat.format(investment.getMaturityDate()) %>'
                    )"
                    class="button button--details">Vender</button>
            </div>
        <%
                }
            } else {
        %>
            <div class="investments-table__row">
                <div style="grid-column: 1 / -1; text-align: center; padding: 2rem;">
                    Você ainda não possui investimentos.
                </div>
            </div>
        <%
            }
        %>
    </div>
</div>

<!-- Modal de Resgate -->
<div id="redeem-modal" class="modal" style="display: none;">
    <div class="modal__content">
        <span class="modal__close" onclick="closeRedeemModal()">&times;</span>
        <h2>Resgatar Investimento</h2>
        <header class="redeem-modal__header stat-card">
            <h3 id="redeem-modal__title"></h3>
            <div class="redeem-modal__items">
                <div class="redeem-modal__item">
                    <span class="redeem-modal__label">Valor Investido:</span>
                    <span class="redeem-modal__value" id="redeem-modal__amount-invested"></span>
                </div>
                <div class="redeem-modal__item">
                    <span class="redeem-modal__label">Valor Atual:</span>
                    <span class="redeem-modal__value redeem-modal__value--success" id="redeem-modal__current-value"></span>
                </div>
                <div class="redeem-modal__item">
                    <span class="redeem-modal__label">Retorno Anual:</span>
                    <span class="redeem-modal__value" id="redeem-modal__annual-return"></span>
                </div>
                <div class="redeem-modal__item">
                    <span class="redeem-modal__label">Vencimento:</span>
                    <span class="redeem-modal__value" id="redeem-modal__expiration-date"></span>
                </div>
            </div>
        </header>
        <form class="redeem-modal__form" action="${pageContext.request.contextPath}/eu/investimentos" method="post">
            <input type="hidden" id="redeem-category" name="category" />
            <input type="hidden" id="redeem-year" name="year" />
            <div class="form-group">
                <label for="redeem-amount">Valor a resgatar (R$):</label>
                <input type="text" id="redeem-amount" name="amount" step="0.01" min="0.01" placeholder="0,00" required>
                <small>Valor máximo disponível: R$ <span id="max-amount">0,00</span></small>
            </div>
            <div class="form-actions">
                <button type="button" onclick="closeRedeemModal()" class="button button--secondary">Cancelar</button>
                <button type="submit" class="button button--primary">Confirmar Resgate</button>
            </div>
        </form>
    </div>
</div>

<script>
    document.getElementById('redeem-amount').addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        value = (value / 100).toLocaleString('pt-BR', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        });
        e.target.value = value;
    });

    function openRedeemModal(category, year, title, amountInvested, currentValue, annualReturn, expirationDate) {
        document.getElementById('redeem-category').value = category;
        document.getElementById('redeem-year').value = year;
        
        document.getElementById('redeem-modal__title').textContent = title;
        document.getElementById('redeem-modal__amount-invested').textContent = 
            parseFloat(amountInvested).toLocaleString('pt-BR', { 
                style: 'currency', 
                currency: 'BRL',
                minimumFractionDigits: 2, 
                maximumFractionDigits: 2 
            });
        document.getElementById('redeem-modal__current-value').textContent = 
            parseFloat(currentValue).toLocaleString('pt-BR', { 
                style: 'currency', 
                currency: 'BRL',
                minimumFractionDigits: 2, 
                maximumFractionDigits: 2 
            });
        document.getElementById('redeem-modal__annual-return').textContent = annualReturn;
        document.getElementById('redeem-modal__expiration-date').textContent = expirationDate;
        
        document.getElementById('max-amount').textContent = 
            parseFloat(amountInvested).toLocaleString('pt-BR', { 
                minimumFractionDigits: 2, 
                maximumFractionDigits: 2 
            });
        document.getElementById('redeem-amount').max = amountInvested;
        
        document.getElementById('redeem-modal').style.display = 'flex';
    }

    function closeRedeemModal() {
        document.getElementById('redeem-modal').style.display = 'none';
        document.getElementById('redeem-amount').value = '';
        
        document.getElementById('redeem-category').value = '';
        document.getElementById('redeem-year').value = '';
    }

    window.onclick = function(event) {
        const modal = document.getElementById('redeem-modal');
        if (event.target == modal) {
            closeRedeemModal();
        }
    }
</script>