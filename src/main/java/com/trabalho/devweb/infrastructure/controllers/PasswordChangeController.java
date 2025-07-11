package com.trabalho.devweb.infrastructure.controllers;

import com.trabalho.devweb.domain.Account;
import com.trabalho.devweb.infrastructure.databaseconnection.PostgresConnection;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/alterar-senha")
public class PasswordChangeController extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validações
        if (currentPassword == null || newPassword == null || confirmPassword == null ||
            currentPassword.trim().isEmpty() || newPassword.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
            
            request.setAttribute("errorMessage", "Todos os campos são obrigatórios");
            request.setAttribute("tab", "security");
            request.getRequestDispatcher("/settings/settings.jsp").forward(request, response);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "A nova senha e a confirmação não coincidem");
            request.setAttribute("tab", "security");
            request.getRequestDispatcher("/settings/settings.jsp").forward(request, response);
            return;
        }
        
        if (newPassword.length() < 6) {
            request.setAttribute("errorMessage", "A nova senha deve ter pelo menos 6 caracteres");
            request.setAttribute("tab", "security");
            request.getRequestDispatcher("/settings/settings.jsp").forward(request, response);
            return;
        }
        
        try {
            if (!verifyCurrentPassword(account.getId(), currentPassword)) {
                request.setAttribute("errorMessage", "Senha atual incorreta");
                request.setAttribute("tab", "security");
                request.getRequestDispatcher("/settings/settings.jsp").forward(request, response);
                return;
            }
            
    
            if (updatePassword(account.getId(), newPassword)) {
                request.setAttribute("successMessage", "Senha atualizada com sucesso!");
            } else {
                request.setAttribute("errorMessage", "Erro ao atualizar senha. Tente novamente.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erro interno do servidor");
        }
        
        // Configurar o parâmetro tab antes do forward
        request.setAttribute("tab", "security");
        request.getRequestDispatcher("/settings/settings.jsp").forward(request, response);
    }
    
    private boolean verifyCurrentPassword(String accountId, String currentPassword) throws SQLException {
        String sql = "SELECT password FROM account WHERE id = ?";
        
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, accountId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                return BCrypt.checkpw(currentPassword, hashedPassword);
            }
            
            return false;
        }
    }
    
    private boolean updatePassword(String accountId, String newPassword) throws SQLException {
        String sql = "UPDATE account SET password = ? WHERE id = ?";
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        
        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, hashedPassword);
            stmt.setString(2, accountId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
