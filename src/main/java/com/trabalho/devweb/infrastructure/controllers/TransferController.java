package com.trabalho.devweb.infrastructure.controllers;

import com.trabalho.devweb.domain.Account;
import com.trabalho.devweb.domain.Transfer;
import com.trabalho.devweb.infrastructure.databaseconnection.PostgresConnection;
import com.trabalho.devweb.infrastructure.repositories.AccountsRepository;
import com.trabalho.devweb.infrastructure.repositories.TransfersRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "TransferController", urlPatterns = "/transferir")
public class TransferController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        try (Connection conn = PostgresConnection.getConnection()) {
            TransfersRepository transRepo = new TransfersRepository(conn);
            // Buscando últimas 3 transferências enviadas
            List<Transfer> recentTransfers = transRepo.findTransfersByAccount(account.getId(), 0, 3, "sent", "");
            req.setAttribute("recentTransfers", recentTransfers);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Erro ao carregar transferências recentes.");
        }

        req.getRequestDispatcher("/transfer.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        Account sender = (Account) session.getAttribute("account");

        if (sender == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String cpf = req.getParameter("cpf");
        String amountStr = req.getParameter("amount");
        String description = req.getParameter("description");

        String error = null;
        String success = null;
        BigDecimal amount = null;

        if (cpf == null || cpf.isBlank() || amountStr == null || amountStr.isBlank()) {
            error = "Preencha todos os campos obrigatórios.";
        } else {
            try {
                // Forçar ponto como separador decimal (ex: 200.00)
                amount = new BigDecimal(amountStr.replace(".", "").replace(",", "."));
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    error = "O valor da transferência deve ser maior que zero.";
                }
            } catch (NumberFormatException e) {
                error = "Formato do valor inválido.";
            }
        }

        if (error == null) {
            try (Connection conn = PostgresConnection.getConnection()) {
                conn.setAutoCommit(false);

                AccountsRepository accRepo = new AccountsRepository(conn);
                TransfersRepository transRepo = new TransfersRepository(conn);

                String formattedCpf = cpf.replaceAll("\\D", "");
                Account recipient = accRepo.findOneByCpf(formattedCpf);

                if (recipient == null) {
                    error = "Destinatário não encontrado.";
                } else if (recipient.getId().equals(sender.getId())) {
                    error = "Não é possível transferir para você mesmo.";
                } else if (sender.getBalance().compareTo(amount) < 0) {
                    error = "Saldo insuficiente.";
                }

                if (error == null) {
                    // Debitar do remetente (localmente)
                    sender.debit(amount);

                    // Atualiza saldo no banco para remetente
                    accRepo.updateAccount(sender);

                    // Creditar no destinatário
                    BigDecimal recipientNewBalance = recipient.getBalance().add(amount);
                    recipient.setBalance(recipientNewBalance);
                    accRepo.updateAccount(recipient);

                    // Registrar transação
                    Transfer transfer = new Transfer();
                    transfer.setOriginId(sender.getId());
                    transfer.setTargetId(recipient.getId());
                    transfer.setCreatedAt(LocalDateTime.now());
                    transfer.setType("transfer");
                    transfer.setAmount(amount);
                    transfer.setDescription(description);
                    transfer.setBalanceAfter(sender.getBalance());

                    transRepo.save(transfer);

                    conn.commit();

                    // Atualiza a conta na sessão (com saldo atualizado)
                    sender = accRepo.findById(sender.getId());
                    session.setAttribute("account", sender);

                    success = "Transferência realizada com sucesso!";
                } else {
                    conn.rollback();
                }
            } catch (Exception e) {
                error = "Erro ao processar a transferência: " + e.getMessage();
                e.printStackTrace();
            }
        }

        // Recarregar últimas 3 transferências para exibir na página
        try (Connection conn = PostgresConnection.getConnection()) {
            TransfersRepository transRepo = new TransfersRepository(conn);
            List<Transfer> recentTransfers = transRepo.findTransfersByAccount(sender.getId(), 0, 3, "sent", "");
            req.setAttribute("recentTransfers", recentTransfers);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        req.setAttribute("error", error);
        req.setAttribute("success", success);
        req.getRequestDispatcher("/transfer.jsp").forward(req, resp);
    }
}
