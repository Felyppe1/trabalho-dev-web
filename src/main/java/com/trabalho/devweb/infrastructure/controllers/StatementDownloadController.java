package com.trabalho.devweb.infrastructure.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.List;

import jakarta.servlet.annotation.WebServlet;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.trabalho.devweb.domain.Account;
import com.trabalho.devweb.domain.Transaction;
import com.trabalho.devweb.infrastructure.databaseconnection.PostgresConnection;
import com.trabalho.devweb.infrastructure.repositories.TransactionRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "StatementDownloadController", urlPatterns = "/extrato/download")
public class StatementDownloadController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Account account = (Account) request.getSession().getAttribute("account");

        int month = Integer.parseInt(request.getParameter("month"));
        int year = Integer.parseInt(request.getParameter("year"));

        System.out.println(">> Account ID: " + account.getName());
        System.out.println(">> Mês: " + month);
        System.out.println(">> Ano: " + year);

        try (Connection connection = PostgresConnection.getConnection();) {
            TransactionRepository transactionRepository = new TransactionRepository(connection);
            List<Transaction> transactions = transactionRepository.findByAccountIdAndMonth(account.getId(), month,
                    year);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=extrato_" + month + "_" + year + ".pdf");

            OutputStream out = response.getOutputStream();
            generatePDF(transactions, out, month, year, account);
        }

        catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao gerar PDF");
        }

    }

    private void generatePDF(List<Transaction> transactions, OutputStream out, int month, int year, Account account)
            throws Exception {

        try {
            System.setProperty("java.awt.headless", "true");

            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Extrato de " + account.getName()));
            document.add(new Paragraph("Mês: " + month + " de " + year));
            document.add(new Paragraph(" "));

            for (Transaction t : transactions) {
                // Formatar o tipo de transação
                String formattedType = formatTransactionType(t, account.getId());

                document.add(new Paragraph(
                        t.getCreatedAt() + " - " + formattedType + " - R$ " + t.getAmount() + " - Saldo: R$ "
                                + t.getBalanceAfter()));
            }

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private String formatTransactionType(Transaction t, String accountId) {
        String type = t.getType() != null ? t.getType().toUpperCase() : "";

        switch (type) {
            case "DEPOSIT":
                return "Depósito";
            case "WITHDRAW":
                return "Saque";
            case "INVESTMENT":
                return "Compra de investimento";
            case "REDEMPTION":
                return "Venda de investimento";
            case "TRANSFER":
                // Determinar se é transferência enviada ou recebida baseado nos IDs
                if (accountId.equals(t.getOriginId())) {
                    return "Transferência enviada para " + t.getTargetId();
                } else if (accountId.equals(t.getTargetId())) {
                    return "Transferência recebida de " + t.getOriginId();
                } else {
                    return "Transferência";
                }
            default:
                return type;
        }
    }

}
