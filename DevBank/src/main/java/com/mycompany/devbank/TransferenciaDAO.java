package com.mycompany.devbank;

import java.sql.*;
import java.util.*;
import java.math.BigDecimal;

public class TransferenciaDAO {
    private final String url = "jdbc:derby://localhost:1527/devbank1";
    private final String user = "app";
    private final String password = "app";

    public TransferenciaDAO() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Contar total de registros com filtro e busca para paginação
    public int contarTotal(String filtro, String busca) {
        String sql = "SELECT COUNT(*) FROM transferencias WHERE 1=1 ";
        if (!filtro.equals("todos")) {
            if (filtro.equals("enviados")) {
                sql += "AND valor < 0 ";
            } else if (filtro.equals("recebidos")) {
                sql += "AND valor >= 0 ";
            }
        }
        if (busca != null && !busca.trim().isEmpty()) {
            sql += "AND (LOWER(remetente_nome) LIKE ? OR LOWER(destinatario_nome) LIKE ?) ";
        }
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (busca != null && !busca.trim().isEmpty()) {
                String filtroBusca = "%" + busca.toLowerCase() + "%";
                ps.setString(1, filtroBusca);
                ps.setString(2, filtroBusca);
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Buscar transferencias com filtro, busca e paginação
    public List<Transferencia> listarTransferencias(String filtro, String busca, int pagina, int tamanhoPagina) {
        List<Transferencia> lista = new ArrayList<>();
        int offset = (pagina - 1) * tamanhoPagina;

        String sql = "SELECT * FROM transferencias WHERE 1=1 ";
        if (!filtro.equals("todos")) {
            if (filtro.equals("enviados")) {
                sql += "AND valor < 0 ";
            } else if (filtro.equals("recebidos")) {
                sql += "AND valor >= 0 ";
            }
        }
        if (busca != null && !busca.trim().isEmpty()) {
            sql += "AND (LOWER(remetente_nome) LIKE ? OR LOWER(destinatario_nome) LIKE ?) ";
        }
        sql += "ORDER BY id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = con.prepareStatement(sql)) {

            int idx = 1;
            if (busca != null && !busca.trim().isEmpty()) {
                String filtroBusca = "%" + busca.toLowerCase() + "%";
                ps.setString(idx++, filtroBusca);
                ps.setString(idx++, filtroBusca);
            }
            ps.setInt(idx++, offset);
            ps.setInt(idx++, tamanhoPagina);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Transferencia t = new Transferencia();
                t.setId(rs.getInt("id"));
                t.setRemetenteNome(rs.getString("remetente_nome"));
                t.setRemetenteBanco(rs.getString("remetente_banco"));
                t.setRemetenteConta(rs.getString("remetente_conta"));
                t.setDestinatarioNome(rs.getString("destinatario_nome"));
                t.setDestinatarioBanco(rs.getString("destinatario_banco"));
                t.setDestinatarioConta(rs.getString("destinatario_conta"));
                t.setValor(rs.getBigDecimal("valor"));
                t.setStatus(rs.getString("status"));
                t.setDataTransferencia(rs.getTimestamp("data_transferencia"));
                lista.add(t);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Métodos para calcular totalEnviado, totalRecebido e saldo
    public BigDecimal totalEnviado() {
        String sql = "SELECT COALESCE(SUM(valor),0) FROM transferencias WHERE valor < 0";
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getBigDecimal(1).abs();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal totalRecebido() {
        String sql = "SELECT COALESCE(SUM(valor),0) FROM transferencias WHERE valor >= 0";
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getBigDecimal(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal saldo() {
        String sql = "SELECT COALESCE(SUM(valor),0) FROM transferencias";
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getBigDecimal(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
    
    public List<DestinatarioFrequente> listarFrequentes() {
        List<DestinatarioFrequente> lista = new ArrayList<>();

        String sql =
            "SELECT destinatario_nome, destinatario_banco, destinatario_conta, COUNT(*) as total " +
            "FROM transferencias " +
            "WHERE valor < 0 " +
            "GROUP BY destinatario_nome, destinatario_banco, destinatario_conta " +
            "ORDER BY total DESC " +
            "FETCH FIRST 3 ROWS ONLY";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String nome = rs.getString("destinatario_nome");
                String banco = rs.getString("destinatario_banco");
                String conta = rs.getString("destinatario_conta");
                lista.add(new DestinatarioFrequente(nome, banco, conta));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}
