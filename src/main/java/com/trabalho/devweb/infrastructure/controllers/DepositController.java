package com.trabalho.devweb.infrastructure.controllers;

import com.trabalho.devweb.domain.Account;
import com.trabalho.devweb.domain.Transaction;
import com.trabalho.devweb.infrastructure.databaseconnection.PostgresConnection;
import com.trabalho.devweb.infrastructure.repositories.AccountsRepository;
import com.trabalho.devweb.infrastructure.repositories.TransactionRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/deposito")
public class DepositController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect to home with deposit action
        response.sendRedirect(request.getContextPath() + "/home?action=deposit");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        String amountStr = request.getParameter("amount");
        String description = request.getParameter("description");

        // Validações
        if (amountStr == null || amountStr.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Valor é obrigatório");
            request.setAttribute("action", "deposit");
            request.getRequestDispatcher("/WEB-INF/pages/home/home.jsp").forward(request, response);
            return;
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new NumberFormatException("Valor deve ser positivo");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Valor inválido");
            request.setAttribute("action", "deposit");
            request.getRequestDispatcher("/WEB-INF/pages/home/home.jsp").forward(request, response);
            return;
        }

        try {
            // Processar depósito
            if (processDeposit(account.getId(), amount, description)) {
                // Atualizar saldo na sessão
                Account updatedAccount = getUpdatedAccount(account.getId());
                session.setAttribute("account", updatedAccount);

                request.setAttribute("successMessage",
                        String.format("Depósito de R$ %.2f realizado com sucesso!",
                                amount.doubleValue()));
            } else {
                request.setAttribute("errorMessage", "Erro ao processar depósito. Tente novamente.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erro interno do servidor");
        }

        // Fetch updated transactions to show in the recent transactions section
        try (Connection connection = PostgresConnection.getConnection()) {
            TransactionRepository transactionRepository = new TransactionRepository(connection);
            List<Transaction> recentTransactions = transactionRepository.findRecentByAccountId(account.getId(), 5);
            request.setAttribute("recentTransactions", recentTransactions);
        } catch (SQLException e) {
            e.printStackTrace();
            // Continue even if transaction fetching fails
        }

        request.setAttribute("action", "deposit");
        request.getRequestDispatcher("/WEB-INF/pages/home/home.jsp").forward(request, response);
    }

    private boolean processDeposit(String accountId, BigDecimal amount, String description) throws SQLException {
        String updateBalanceSQL = "UPDATE account SET balance = balance + ? WHERE id = ?";
        String insertTransactionSQL = "INSERT INTO transaction (id, origin_id, target_id, type, amount, description, balance_after, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = PostgresConnection.getConnection()) {
            conn.setAutoCommit(false);

            try {
                BigDecimal balanceAfter;

                // Atualizar saldo
                try (PreparedStatement stmt = conn.prepareStatement(updateBalanceSQL)) {
                    stmt.setBigDecimal(1, amount);
                    stmt.setString(2, accountId);
                    stmt.executeUpdate();
                }

                // Obter o saldo atualizado
                String getBalanceSQL = "SELECT balance FROM account WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(getBalanceSQL)) {
                    stmt.setString(1, accountId);
                    try (java.sql.ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            balanceAfter = rs.getBigDecimal("balance");
                        } else {
                            throw new SQLException("Conta não encontrada");
                        }
                    }
                }

                // Inserir transação
                try (PreparedStatement stmt = conn.prepareStatement(insertTransactionSQL)) {
                    // Gerar um UUID para o id da transação
                    String transactionId = java.util.UUID.randomUUID().toString();

                    stmt.setString(1, transactionId); // id da transação
                    stmt.setString(2, null); // origin_id is null for deposits (no origin account)
                    stmt.setString(3, accountId); // target_id is the account receiving the deposit
                    stmt.setString(4, "DEPOSIT");
                    stmt.setBigDecimal(5, amount);

                    String transactionDescription = description != null && !description.trim().isEmpty()
                            ? description.trim()
                            : "Depósito";
                    stmt.setString(6, transactionDescription);
                    stmt.setBigDecimal(7, balanceAfter);
                    stmt.setObject(8, LocalDateTime.now());
                    stmt.executeUpdate();
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    private Account getUpdatedAccount(String accountId) throws SQLException {
        try (Connection conn = PostgresConnection.getConnection()) {
            AccountsRepository accountRepository = new AccountsRepository(conn);
            return accountRepository.findById(accountId);
        }
    }
}
