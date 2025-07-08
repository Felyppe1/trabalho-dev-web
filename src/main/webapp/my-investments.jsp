<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Meus Investimentos - Digital Bank</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/components/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/my-investments.css">
</head>
<body>
    <%@ include file="components/header.jsp" %>
    
    <div class="investments">
        <div class="tabs">
            <button class="tabs__item">Available Investments</button>
            <button class="tabs__item tabs__item--active">My Investments</button>
        </div>

        <div class="card">
            <div class="portfolio">
                <h1 class="portfolio__title">My Investment Portfolio</h1>
                <p class="portfolio__subtitle">Your current investments and their performance</p>
            </div>

            <div class="investments-table">
                <div class="investments-table__header">
                    <div>Title</div>
                    <div>Annual Return</div>
                    <div>Amount Invested</div>
                    <div>Current Value</div>
                    <div>Maturity</div>
                    <div></div>
                </div>

                <div class="investments-table__row">
                    <div class="investment">
                        <div class="investment__indicator investment__indicator--prefixed"></div>
                        <span class="investment__name investment__name--prefixed">TESOURO PREFIXADO 2028</span>
                    </div>
                    <div class="investment__return">13,98%</div>
                    <div class="investment__amount">R$ 10.000,00</div>
                    <div class="investment__value">R$ 10.845,30</div>
                    <div class="investment__maturity">01/01/2028</div>
                    <a href="#" class="button">Details</a>
                </div>

                <div class="investments-table__row">
                    <div class="investment">
                        <div class="investment__indicator investment__indicator--selic"></div>
                        <span class="investment__name investment__name--selic">TESOURO SELIC 2031</span>
                    </div>
                    <div class="investment__return">SELIC + 0,1152%</div>
                    <div class="investment__amount">R$ 10.000,00</div>
                    <div class="investment__value">R$ 10.845,30</div>
                    <div class="investment__maturity">01/03/2031</div>
                    <a href="#" class="button">Details</a>
                </div>
            </div>
        </div>

    </div>

    <script>
        // Funcionalidade dos botões de detalhes
        document.querySelectorAll('.button').forEach(btn => {
            btn.addEventListener('click', function() {
                if (this.textContent.trim() === 'Details') {
                    const row = this.closest('.investments-table__row');
                    const investmentName = row.querySelector('.investment__name').textContent;
                    alert('Mostrando detalhes para: ' + investmentName);
                }
            });
        });

        // Funcionalidade das abas
        document.querySelectorAll('.tabs__item').forEach(tab => {
            tab.addEventListener('click', function() {
                document.querySelectorAll('.tabs__item').forEach(t => t.classList.remove('tabs__item--active'));
                this.classList.add('tabs__item--active');
                
                if (this.textContent.trim() === 'Available Investments') {
                    // Redirecionar para página de investimentos disponíveis
                    console.log('Redirecionando para investimentos disponíveis');
                }
            });
        });
    </script>
</body>
</html>